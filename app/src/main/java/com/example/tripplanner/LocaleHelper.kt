package com.example.tripplanner

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

object LocaleHelper {

    private const val PREFS_NAME = "trip_planner_prefs"
    private const val KEY_LANGUAGE = "app_language"
    private const val KEY_THEME = "app_theme"

    //applying saved lang and theme on every screen
    fun applySettings(context: Context): Context {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        //theme
        val isDarkMode = prefs.getBoolean(KEY_THEME, false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        //language
        val lang = prefs.getString(KEY_LANGUAGE, "en") ?: "en"
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    //saving the language
    fun setLanguage(context: Context, langCode: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, langCode).apply()
    }

    //getting the saved language
    fun getLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, "en") ?: "en"
    }

    //saving the theme
    fun setTheme(context: Context, isDarkMode: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_THEME, isDarkMode).apply()
    }

    //getting the saved theme
    fun isDarkMode(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_THEME, false)
    }
}