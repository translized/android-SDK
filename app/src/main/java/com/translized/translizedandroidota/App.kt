package com.translized.translizedandroidota

import android.app.Application
import com.translized.translized_ota.Translized
import com.translized.translized_ota.TranslizedCallback
import com.translized.translized_ota.TranslizedError

/**
 * TODO: Add type description!
 *
 * Created by nikolatomovic on 11.1.22..
 * Copyright © 2022 aktiia SA. All rights reserved.
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Translized.init(this,"9AXGSpetMe", "123")
        Translized.addCallback(object : TranslizedCallback{
            override fun onTranslationsUpdated() {
            }

            override fun onFailure(error: TranslizedError) {
            }

            override fun onUpdateNotNeeded() {
            }

        })
        Translized.updateTranslations()
    }
}