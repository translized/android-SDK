package com.translized.translized_ota

import android.content.Context
import org.json.JSONObject
import java.io.File
import java.util.*

/**
 * TODO: Add type description!
 *
 * Created by nikolatomovic on 28.12.21..
 * Copyright © 2021 aktiia SA. All rights reserved.
 */
class TranslizedRepository {
    private var texts: Map<String, String?> = emptyMap()
    private var plurals: Map<String, String> = emptyMap()

    fun getText(id: String): String? = texts[id]
    fun getPlural(id: String): String? = plurals[id]

    fun loadTranslations(context: Context) {
        val languageCode = Locale.getDefault().language
        val filePath = context.filesDir.absolutePath  + "/translized_$languageCode.json"
        texts = JSONObject(File(filePath).readText()).toMap()
    }
}