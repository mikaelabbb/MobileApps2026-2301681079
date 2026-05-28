package com.example.tripplanner

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tripplanner.data.Trip
import com.example.tripplanner.viewmodel.TripViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddEditTripActivity : AppCompatActivity() {

    private val tripViewModel: TripViewModel by viewModels()
    private var currentTripId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_trip)

        currentTripId = intent.getIntExtra("TRIP_ID", -1)

        //bottom nav
        val bottomNav = findViewById<BottomNavigationView>(
            R.id.bottomNavigation
        )

        BottomNavHelper.setup(bottomNav, this)

        bottomNav.selectedItemId = R.id.nav_add

        //finding the UI files
        val etDestination = findViewById<TextInputEditText>(R.id.editTextDestination)
        val etStartDate = findViewById<TextInputEditText>(R.id.editTextStartDate)
        val etEndDate = findViewById<TextInputEditText>(R.id.editTextEndDate)
        val etDescription = findViewById<TextInputEditText>(R.id.editTextDescription)
        val btnSave = findViewById<Button>(R.id.buttonSave)

        //start date
        etStartDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Choose a start date")
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                etStartDate.setText(dateFormatter.format(Date(selection)))
            }
            datePicker.show(supportFragmentManager, "START_DATE_PICKER")
        }

        //end date
        etEndDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Choose an end date")
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                etEndDate.setText(dateFormatter.format(Date(selection)))
            }
            datePicker.show(supportFragmentManager, "END_DATE_PICKER")
        }

        //save trip logic (create)
        btnSave.setOnClickListener {

            val destination = etDestination.text.toString().trim()
            val startDate = etStartDate.text.toString().trim()
            val endDate = etEndDate.text.toString().trim()
            val description = etDescription.text.toString().trim()

            if (destination.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Please, fill all fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val trip = Trip(
                id = if (currentTripId != -1) currentTripId else 0,
                destination = destination,
                startDate = startDate,
                endDate = endDate,
                description = description
            )

            if (currentTripId != -1) {
                tripViewModel.update(trip)
                Toast.makeText(this, "Trip updated!", Toast.LENGTH_SHORT).show()
            } else {
                tripViewModel.insert(trip)
                Toast.makeText(this, "Trip saved successfully!", Toast.LENGTH_SHORT).show()
            }

            finish()
        }

        //edit mode
        if (currentTripId != -1) {

            btnSave.text = "Update Trip"

            tripViewModel.allTrips.observe(this) { trips ->
                val trip = trips.find { it.id == currentTripId }

                trip?.let {
                    etDestination.setText(it.destination)
                    etStartDate.setText(it.startDate)
                    etEndDate.setText(it.endDate)
                    etDescription.setText(it.description)
                }
            }
        }
    }
}