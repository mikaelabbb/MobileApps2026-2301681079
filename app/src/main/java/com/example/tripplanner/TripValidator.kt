package com.example.tripplanner

import java.text.SimpleDateFormat
import java.util.Locale

object TripValidator {

    fun hasRequiredFields(
        destination: String,
        startDate: String,
        endDate: String
    ): Boolean {
        return destination.isNotBlank()
                && startDate.isNotBlank()
                && endDate.isNotBlank()
    }

    fun areDatesValid(
        startDate: String,
        endDate: String
    ): Boolean {

        val formatter = SimpleDateFormat(
            "dd.MM.yyyy",
            Locale.getDefault()
        )

        val start = formatter.parse(startDate)
        val end = formatter.parse(endDate)

        return start != null &&
                end != null &&
                !end.before(start)
    }
}