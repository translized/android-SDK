package com.translized.translized_ota

import android.view.View
import io.github.inflationx.viewpump.InflateResult
import io.github.inflationx.viewpump.Interceptor

/**
 * TODO: Add type description!
 *
 * Created by nikolatomovic on 10.1.22..
 * Copyright © 2022 aktiia SA. All rights reserved.
 */

internal class TranslizedUpdateTranslationsInterceptor(
    private val transcriptionManager: TranscriptionManager
) : Interceptor {

    private val transcriberCache: MutableMap<Class<*>, Transcriber<*>?> = mutableMapOf()

    override fun intercept(chain: Interceptor.Chain): InflateResult {
        val result = chain.proceed(chain.request())
        val view = result.view ?: return result

        transcribeView(view, result)

        return result
    }

    private fun transcribeView(view: View, result: InflateResult) {
        val clazz = view.javaClass
        val transcriber = fetchTranscriber(clazz)

        transcriber?.transcribe(view, result.attrs!!)
    }

    private fun <T : View> fetchTranscriber(clazz: Class<T>): Transcriber<T>? {
        if (transcriberCache.containsKey(clazz)) {
            @Suppress("UNCHECKED_CAST")
            return transcriberCache[clazz] as Transcriber<T>?
        }

        val transcriber = transcriptionManager.nextTranscriber(clazz)
        transcriberCache[clazz] = transcriber
        return transcriber
    }
}
