package com.example.tripplanner

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val activityRule =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainScreen_isDisplayed() {

        onView(withId(R.id.welcomeText))
            .check(matches(isDisplayed()))

        onView(withId(R.id.recyclerViewTrips))
            .check(matches(isDisplayed()))

        onView(withId(R.id.bottomNavigation))
            .check(matches(isDisplayed()))
    }
}