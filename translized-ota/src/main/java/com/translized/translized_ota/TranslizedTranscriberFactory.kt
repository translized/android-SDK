package com.translized.translized_ota

import android.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * TODO: Add type description!
 *
 * Created by nikolatomovic on 10.1.22..
 * Copyright © 2022 aktiia SA. All rights reserved.
 */

internal class TranslizedTranscriberFactory : Transcriber.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : View> createTranscriber(
        clazz: Class<T>,
        transcriptionManager: TranscriptionManager
    ): Transcriber<T>? {
        return when {
            TextView::class.java.isAssignableFrom(clazz) -> {
                Transcriber.withMappings<TextView>(transcriptionManager) { mappings ->
                    mappings(R.attr.text) { v, text -> v.text = text }
                    mappings(R.attr.hint) { v, text -> v.hint = text }
                    mappings(R.attr.contentDescription) { v, text ->
                        v.contentDescription = text
                    }
                }
            }
            ImageView::class.java.isAssignableFrom(clazz) -> {
                Transcriber.withMappings<ImageView>(transcriptionManager) { mappings ->
                    mappings(R.attr.contentDescription) { v, text ->
                        v.contentDescription = text
                    }
                }
            }
            else -> {
                null
            }
        } as Transcriber<T>?
    }
}