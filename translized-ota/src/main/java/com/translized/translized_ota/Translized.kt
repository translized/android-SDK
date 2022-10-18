package com.translized.translized_ota

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import com.translized.translized_ota.api.ApiInterface
import android.content.pm.PackageManager
import com.translized.translized_ota.local.TranslizedConstants
import com.translized.translized_ota.local.TranslizedUtils
import com.translized.translized_ota.model.OtaRelease
import com.translized.translized_ota.model.OtaResult
import com.translized.translized_ota.model.RequestCheckOta
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

public object Translized {
    private var isInitialized = false
    private val repository: TranslizedRepository = TranslizedRepository()
    private var downloader = TranslizedDownloader()
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var application: Application
    private lateinit var projectId: String
    private lateinit var otaToken: String
    private lateinit var transcriptionManager: TranscriptionManager
    private var clientCallback: TranslizedCallback? = null


    fun init(application: Application, projectId: String, otaToken: String, transcribers: List<Transcriber.Factory> = emptyList()) {
        check(!isInitialized) { "Already initialized. Only call this once!" }
        isInitialized = true
        Translized.application = application
        Translized.projectId = projectId
        Translized.otaToken = otaToken
        sharedPrefs = application.applicationContext.getSharedPreferences(
            TranslizedConstants.PREFERENCES.value,
            Context.MODE_PRIVATE
        )
        transcriptionManager = TranscriptionManager(TranslizedResources(repository, application.resources), transcribers)
        val viewPump = ViewPump.builder()
            .addInterceptor(TranslizedUpdateTranslationsInterceptor(transcriptionManager))
            .build()
        ViewPump.init(viewPump)
        if (sharedPrefs.getString(TranslizedConstants.OTA_ID.value, null) != null) {
            repository.loadTranslations(Translized.application.applicationContext)
        }
    }

    public fun updateTranslations() {
        check(isInitialized) { "Not initialized. Call `Translized.init(context)` from your Application." }
        val requestCheckOta = getCheckOtaRequest() ?: return
        val apiInterface = ApiInterface.create().checkOta(requestCheckOta)
        apiInterface.enqueue(object: Callback<OtaResult>{
            override fun onResponse(call: Call<OtaResult>, response: Response<OtaResult>) {
                if (response.isSuccessful) {
                    response.body()?.result?.let {
                        downloadFiles(it)
                        if (it.objectId == null) {
                            clientCallback?.onUpdateNotNeeded()
                        }
                    }
                    val editor = sharedPrefs.edit()
                    editor.putString(TranslizedConstants.LAST_CHECK_DATE.value, TranslizedUtils.getISO8601StringForCurrentDate())
                    editor.apply()
                } else {
                    clientCallback?.onFailure(TranslizedError(response.errorBody()?.string() ?: "Something went wrong."))
                }
            }

            override fun onFailure(call: Call<OtaResult>, t: Throwable) {
                clientCallback?.onFailure(TranslizedError(t.localizedMessage ?: "Something went wrong."))
            }
        })

    }

    public fun addCallback(callback: TranslizedCallback) {
        clientCallback = callback
    }

    private fun downloadFiles(otaRelease: OtaRelease) {
        val otaReleaseId = otaRelease.objectId ?: return
        val files = otaRelease.files ?: return
        downloader.downloadFiles(application.applicationContext, files, object: TranslizedCallback {
            override fun onTranslationsUpdated() {
                val editor = sharedPrefs.edit()
                editor.putString(TranslizedConstants.OTA_ID.value, otaReleaseId)
                editor.apply()
                repository.loadTranslations(application.applicationContext)
                clientCallback?.onTranslationsUpdated()
            }

            override fun onFailure(error: TranslizedError) {
                clientCallback?.onFailure(error)
            }

            override fun onUpdateNotNeeded() {
                clientCallback?.onUpdateNotNeeded()
            }
        })
    }

    public fun wrapContext(baseContext: Context?): Context? {
        check(isInitialized) { "Not initialized. Call `Translized.init(context)` from your Application." }
        baseContext?.let {
            return TranslizedContextWrapper(ViewPumpContextWrapper.wrap(it), repository)
        }
        return  baseContext
    }

    private fun getCheckOtaRequest(): RequestCheckOta? {
        val versionName: String
        try {
            versionName = application.applicationContext.packageManager
                .getPackageInfo(application.applicationContext.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return null
        }

        val currentOtaId = sharedPrefs.getString(TranslizedConstants.OTA_ID.value, "") ?: ""
        val lastCheckDate = sharedPrefs.getString(TranslizedConstants.LAST_CHECK_DATE.value, "") ?: ""

        val currentUserId = sharedPrefs.getString(TranslizedConstants.USER_ID.value, null)
        val userId = currentUserId ?: UUID.randomUUID().toString()
        if (currentUserId == null) {
            val editor = sharedPrefs.edit()
            editor.putString(TranslizedConstants.USER_ID.value, userId)
            editor.apply()
        }
        return RequestCheckOta(
            projectId,
            otaToken,
            versionName,
            currentOtaId,
            userId,
            "android",
            lastCheckDate
        )
    }

    private class TranslizedContextWrapper(
        baseContext: Context,
        private val repository: TranslizedRepository
    ) : ContextWrapper(baseContext) {

        private var translizedResources: Resources? = null

        override fun createConfigurationContext(overrideConfiguration: Configuration): Context {
            // todo what does this break/override?
            // needed for getString to use the right resources, will break bottom nav clicks though
            return this
        }

        override fun getResources(): Resources {
            if (translizedResources == null) {
                synchronized(this) {
                    if (translizedResources == null) {
                        translizedResources = TranslizedResources(repository, super.getResources())
                    }
                }
            }
            return translizedResources!!
        }
    }
}