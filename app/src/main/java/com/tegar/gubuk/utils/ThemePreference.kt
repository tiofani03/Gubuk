package com.tegar.gubuk.utils

import android.content.Context
import android.preference.PreferenceManager

class ThemePreference(context: Context) {
    companion object {
        private const val THEME_STATUS = "status tema"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    //0 jika light dan 1 jika dark
    var theme = preferences.getInt(THEME_STATUS, 0)
        set(value) = preferences.edit().putInt(THEME_STATUS, value).apply()
}