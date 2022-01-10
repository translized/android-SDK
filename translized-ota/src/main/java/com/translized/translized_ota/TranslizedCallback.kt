package com.translized.translized_ota

/**
 * TODO: Add type description!
 *
 * Created by nikolatomovic on 9.1.22..
 * Copyright © 2022 aktiia SA. All rights reserved.
 */
public interface TranslizedCallback {
    fun onTranslationsUpdated()
    fun onFailure(error: TranslizedError)
    fun onUpdateNotNeeded()
}

class TranslizedError(message: String) : Exception(message)