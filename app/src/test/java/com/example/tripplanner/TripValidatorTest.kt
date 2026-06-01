package com.example.tripplanner

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TripValidatorTest {

    @Test
    fun validTrip_returnsTrue() {
        assertTrue(
            TripValidator.hasRequiredFields(
                "Paris",
                "01.06.2026",
                "05.06.2026"
            )
        )
    }

    @Test
    fun emptyDestination_returnsFalse() {
        assertFalse(
            TripValidator.hasRequiredFields(
                "",
                "01.06.2026",
                "05.06.2026"
            )
        )
    }

    @Test
    fun endDateBeforeStartDate_returnsFalse() {
        assertFalse(
            TripValidator.areDatesValid(
                "10.06.2026",
                "05.06.2026"
            )
        )
    }
}