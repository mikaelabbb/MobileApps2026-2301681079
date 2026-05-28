package com.example.tripplanner

import android.app.Activity
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView

object BottomNavHelper {

    fun setup(bottomNav: BottomNavigationView, activity: Activity) {

        bottomNav.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_trips -> {

                    if (activity !is MainActivity) {
                        activity.startActivity(
                            Intent(activity, MainActivity::class.java)
                        )
                    }

                    true
                }

                R.id.nav_add -> {

                    if (activity !is AddEditTripActivity) {
                        activity.startActivity(
                            Intent(activity, AddEditTripActivity::class.java)
                        )
                    }

                    true
                }

                R.id.nav_settings -> {

                    if (activity !is SettingsActivity) {
                        activity.startActivity(
                            Intent(activity, SettingsActivity::class.java)
                        )
                    }

                    true
                }

                else -> false
            }
        }
    }
}