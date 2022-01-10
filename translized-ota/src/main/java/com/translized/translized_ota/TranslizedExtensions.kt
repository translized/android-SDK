package com.translized.translized_ota

import org.json.JSONObject

/**
 * TODO: Add type description!
 *
 * Created by nikolatomovic on 9.1.22..
 * Copyright © 2022 aktiia SA. All rights reserved.
 */

fun JSONObject.toMap(): Map<String, String?> = keys().asSequence().associateWith {
    when (val value = this[it])
    {
        is String -> value
        else      -> null
    }
}