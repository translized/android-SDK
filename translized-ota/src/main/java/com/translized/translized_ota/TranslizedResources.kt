package com.translized.translized_ota

import android.content.res.Resources

/**
 * TODO: Add type description!
 *
 * Created by nikolatomovic on 28.12.21..
 * Copyright © 2021 aktiia SA. All rights reserved.
 */
class TranslizedResources(
    private val repository: TranslizedRepository,
    private val resources: Resources,
) : Resources(resources.assets, resources.displayMetrics, resources.configuration) {

    override fun getString(id: Int): String {
        return getTranslation(id) ?: super.getString(id)
    }

    override fun getString(id: Int, vararg formatArgs: Any?): String {
        return getTranslation(id)?.format(*formatArgs) ?: super.getString(id, *formatArgs)
    }

    override fun getText(id: Int): CharSequence {

        return getTranslation(id) ?: return super.getText(id)
    }

    override fun getText(id: Int, def: CharSequence?): CharSequence {

        return getTranslation(id) ?: return super.getString(id, def)
    }

    override fun getQuantityText(id: Int, quantity: Int): CharSequence {

        return resources.getQuantityText(id, quantity)
    }

    override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any?): String {

        return resources.getQuantityString(id, quantity, *formatArgs)
    }

    override fun getQuantityString(id: Int, quantity: Int): String {

        return resources.getQuantityString(id, quantity)
    }

    override fun getTextArray(id: Int): Array<CharSequence> {
        return resources.getTextArray(id)
    }

    override fun getStringArray(id: Int): Array<String> {
        return resources.getStringArray(id)
    }
    private fun getTranslation(id: Int): String? {
        val name = resources.getResourceEntryName(id)
        return repository.getText(name)
    }
}