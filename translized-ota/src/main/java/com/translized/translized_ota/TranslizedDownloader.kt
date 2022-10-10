package com.translized.translized_ota

import android.content.Context
import android.util.Log
import com.translized.translized_ota.api.ApiInterface
import com.translized.translized_ota.model.OtaFile
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class TranslizedDownloader {

    private var filesToDownload = 0

    fun downloadFiles(context: Context, files: Array<OtaFile>, callback: TranslizedCallback) {
        val apiInterface = ApiInterface.create()
        files.forEach {
            val url = it.fileURL ?: return
            val languageCode = it.languageCode ?: return
            filesToDownload += 1
            apiInterface.downloadFile(url).enqueue(object: Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful){
                        val filePath = context.filesDir.absolutePath  + "/translized_$languageCode.json"
                        saveFile(response.body(), filePath)
                        filesToDownload -= 1
                        if (filesToDownload == 0) {
                            callback.onTranslationsUpdated()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback.onFailure(TranslizedError(t.localizedMessage ?: "Something went wrong."))
                }
            })
        }
    }

    fun saveFile(body: ResponseBody?, filePath: String):String{
        if (body==null)
            return ""
        var input: InputStream? = null
        try {
            input = body.byteStream()
            val fos = FileOutputStream(filePath)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return filePath
        }catch (e:Exception){
            Log.e("saveFile",e.toString())
        }
        finally {
            input?.close()
        }
        return ""
    }
}