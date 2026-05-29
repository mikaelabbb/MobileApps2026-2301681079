package com.example.tripplanner

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.materialswitch.MaterialSwitch

class SettingsActivity : AppCompatActivity() {

    private var isSpinnerFirstCall = true

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.applySettings(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleHelper.applySettings(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val themeSwitch = findViewById<MaterialSwitch>(R.id.themeSwitch)
        val languageSpinner = findViewById<Spinner>(R.id.languageSpinner)
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
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        //getting the saved language
        val savedLang = LocaleHelper.getLanguage(this)
        if (savedLang == "bg") {
            languageSpinner.setSelection(1) //marks bulgarian
        } else {
            languageSpinner.setSelection(0) //marks english
        }

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isSpinnerFirstCall) {
                    isSpinnerFirstCall = false
                    return
                }

                val selectedLang = if (position == 1) "bg" else "en"

                //changing the language only if the user picked different from the current one
                if (selectedLang != savedLang) {
                    LocaleHelper.setLanguage(this@SettingsActivity, selectedLang)
                    LocaleHelper.applySettings(this@SettingsActivity)
                    recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}