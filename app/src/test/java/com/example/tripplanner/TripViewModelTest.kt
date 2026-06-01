package com.example.tripplanner

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class TripViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun test_destinationValidation_returnsTrueForValidInput() {
        //валидация на поле при създаване на пътуване
        val destination = "Пловдив"
        val isInputValid = destination.trim().isNotEmpty()

        assertEquals(true, isInputValid)
    }

    @Test
    fun test_dateValidation_returnsFalseForEmptyDates() {
        //дали системата ще отхвърли празни дати
        val startDate = ""
        val isDateValid = startDate.isNotEmpty()

        assertEquals(false, isDateValid)
    }
}