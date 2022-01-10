package com.translized.translized_ota

import android.content.res.Resources
import android.view.View
import androidx.annotation.StringRes

/**
 * TODO: Add type description!
 *
 * Created by nikolatomovic on 10.1.22..
 * Copyright © 2022 aktiia SA. All rights reserved.
 */

public class TranscriptionManager(
    private val resources: Resources,
    transcriberFactories: List<Transcriber.Factory>
) {

    private val factories: List<Transcriber.Factory> =
        transcriberFactories + mutableListOf(TranslizedTranscriberFactory())

    public fun <V : View> nextTranscriber(
        clazz: Class<V>,
        from: Transcriber.Factory? = null
    ): Transcriber<V>? {
        val factories = if (from == null) {
            factories.asSequence()
        } else {
            factories.asSequence()
                // skip until the next factory after `from`
                .dropWhile { factory -> factory != from }.drop(1)
        }

        return factories.asSequence()
            .mapNotNull { it.createTranscriber(clazz, this) }
            .firstOrNull()
    }

    public fun updateText(@StringRes id: Int, updateText: (CharSequence) -> Unit) {
        updateText(resources.getText(id))
    }

    public fun getText(@StringRes id: Int): CharSequence =
        if (id == 0) "" else resources.getText(id)
}