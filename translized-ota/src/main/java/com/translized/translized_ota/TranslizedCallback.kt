package com.translized.translized_ota

public interface TranslizedCallback {
    fun onTranslationsUpdated()
    fun onFailure(error: TranslizedError)
    fun onUpdateNotNeeded()
}

class TranslizedError(message: String) : Exception(message)