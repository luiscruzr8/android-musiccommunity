package com.tfm.musiccommunityapp.ui.base

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class StringSharedPreferencesDelegate(
    private val sharedPreferences: SharedPreferences,
    private val defaultValue: String,
    private val key: String? = null
) : ReadWriteProperty<Any, String> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return sharedPreferences.getString(key ?: property.name, null) ?: defaultValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        sharedPreferences.edit { putString(key ?: property.name, value) }
    }
}

fun SharedPreferences.string(key: String? = null, defaultValue: String = "") =
    StringSharedPreferencesDelegate(this, defaultValue, key)