package com.example.tripplanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        //bottom nav
        val bottomNav =
            findViewById<BottomNavigationView>(
                R.id.bottomNavigation
            )

        BottomNavHelper.setup(bottomNav, this)

        bottomNav.selectedItemId = R.id.nav_settings
    }
}