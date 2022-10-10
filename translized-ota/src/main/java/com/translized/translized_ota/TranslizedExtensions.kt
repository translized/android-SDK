package com.translized.translized_ota

import org.json.JSONObject

fun JSONObject.toMap(): Map<String, String?> = keys().asSequence().associateWith {
    when (val value = this[it])
    {
        is String -> value
        else      -> null
    }
}