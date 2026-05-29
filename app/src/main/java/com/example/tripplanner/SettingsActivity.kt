package com.example.tripplanner

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.materialswitch.MaterialSwitch

class SettingsActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.applySettings(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleHelper.applySettings(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val themeSwitch = findViewById<MaterialSwitch>(R.id.themeSwitch)
        val languageSpinner = findViewById<AutoCompleteTextView>(R.id.languageSpinner)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        //bottom nav setup
        BottomNavHelper.setup(bottomNav, this)
        bottomNav.selectedItemId = R.id.nav_settings

        //theme logic
        val isDark = LocaleHelper.isDarkMode(this)

        themeSwitch.setOnCheckedChangeListener(null)
        themeSwitch.isChecked = isDark

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked != LocaleHelper.isDarkMode(this@SettingsActivity)) {
                LocaleHelper.setTheme(this@SettingsActivity, isChecked)
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }

        //changing language logic
        val languages = arrayOf("English", "Български")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, languages)
        languageSpinner.setAdapter(adapter)

        //getting the saved language
        val savedLang = LocaleHelper.getLanguage(this)
        if (savedLang == "bg") {
            languageSpinner.setText("Български", false)
        } else {
            languageSpinner.setText("English", false)
        }

        languageSpinner.setOnItemClickListener { _, _, position, _ ->
            val selectedLang = if (position == 1) "bg" else "en"

            //changing the language only if the user picked different from the current one
            if (selectedLang != savedLang) {
                LocaleHelper.setLanguage(this@SettingsActivity, selectedLang)
                LocaleHelper.applySettings(this@SettingsActivity)
                recreate()
            }
        }
    }
}