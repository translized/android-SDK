package com.translized.translizedandroidota

import android.app.Application
import com.translized.translized_ota.Translized
import com.translized.translized_ota.TranslizedCallback
import com.translized.translized_ota.TranslizedError

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Translized.init(this,"9AXGSpetMe", "123")
        Translized.addCallback(object : TranslizedCallback{
            override fun onTranslationsUpdated() {
                print("Updated")
            }

            override fun onFailure(error: TranslizedError) {
                print(error.message)
            }

            override fun onUpdateNotNeeded() {
            }

        })
        Translized.updateTranslations()
    }
}