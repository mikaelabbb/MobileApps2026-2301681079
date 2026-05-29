package com.example.tripplanner

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tripplanner.data.Trip
import com.example.tripplanner.viewmodel.TripViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class AddEditTripActivity : AppCompatActivity() {

    private val tripViewModel: TripViewModel by viewModels()
    private var currentTripId = -1

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
        if (result.contents != null) {
            val scannedData = result.contents
            parseAndFillTripData(scannedData)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.applySettings(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_trip)

        currentTripId = intent.getIntExtra("TRIP_ID", -1)

        //bottom nav
        val bottomNav = findViewById<BottomNavigationView>(
            R.id.bottomNavigation
        )

        BottomNavHelper.setup(bottomNav, this)
        bottomNav.selectedItemId = R.id.nav_new_trip

        //finding the UI files
        val etDestination = findViewById<TextInputEditText>(R.id.editTextDestination)
        val etStartDate = findViewById<TextInputEditText>(R.id.editTextStartDate)
        val etEndDate = findViewById<TextInputEditText>(R.id.editTextEndDate)
        val etDescription = findViewById<TextInputEditText>(R.id.editTextDescription)
        val btnSave = findViewById<Button>(R.id.buttonSave)

        //start date
        etStartDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.title_date_start))
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
                .setTitleText(getString(R.string.title_date_end))
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

            //checking for empty fields
            if (destination.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Snackbar.make(btnSave, getString(R.string.error_fill_fields), Snackbar.LENGTH_SHORT)
                    .setAnchorView(bottomNav)
                    .setBackgroundTint(getColor(R.color.purple_dark))
                    .show()
                return@setOnClickListener
            }

            //validation with snackbar
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            try {
                val dateStart = dateFormat.parse(startDate)
                val dateEnd = dateFormat.parse(endDate)

                if (dateStart != null && dateEnd != null && dateEnd.before(dateStart)) {
                    Snackbar.make(btnSave, getString(R.string.error_invalid_dates), Snackbar.LENGTH_LONG)
                        .setAnchorView(bottomNav)
                        .setBackgroundTint(getColor(R.color.purple_dark))
                        .show()
                    return@setOnClickListener
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
                Snackbar.make(btnSave, getString(R.string.toast_trip_updated), Snackbar.LENGTH_SHORT)
                    .setAnchorView(bottomNav)
                    .setBackgroundTint(getColor(R.color.purple_dark))
                    .show()
            } else {
                tripViewModel.insert(trip)
                Snackbar.make(btnSave, getString(R.string.toast_trip_saved), Snackbar.LENGTH_SHORT)
                    .setAnchorView(bottomNav)
                    .setBackgroundTint(getColor(R.color.purple_dark))
                    .show()
            }

            //delay so the user can see the message
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 400)
        }

        //edit mode
        if (currentTripId != -1) {
            btnSave.text = getString(R.string.btn_update_trip)

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

        val btnScanQR = findViewById<Button>(R.id.buttonScanQR)

        if (currentTripId != -1) {
            btnScanQR.visibility = android.view.View.GONE
        }

        btnScanQR.setOnClickListener {
            val options = ScanOptions().apply {
                setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                setPrompt(getString(R.string.btn_scan_qr))
                setCameraId(0)
                setBeepEnabled(true)
                setBarcodeImageEnabled(false)
                setOrientationLocked(true)
            }
            barcodeLauncher.launch(options)
        }
    }

    private fun parseAndFillTripData(qrText: String) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        try {
            Snackbar.make(bottomNav, "Read: $qrText", Snackbar.LENGTH_LONG)
                .setAnchorView(bottomNav)
                .setBackgroundTint(getColor(R.color.purple_dark))
                .show()

            val lines = qrText.lines().map { it.trim() }.filter { it.isNotEmpty() }

            var destination = ""
            var startDate = ""
            var endDate = ""
            var description = ""

            for (line in lines) {
                when {
                    line.contains("Trip to", ignoreCase = true) || line.contains("Дестинация", ignoreCase = true) -> {
                        destination = line.substringAfter(":").trim()
                    }
                    line.contains("Dates", ignoreCase = true) || line.contains("Дати", ignoreCase = true) -> {
                        val datesRaw = line.substringAfter(":").trim()
                        val datesSplit = datesRaw.split("-")
                        startDate = datesSplit.getOrNull(0)?.trim() ?: ""
                        endDate = datesSplit.getOrNull(1)?.trim() ?: ""
                    }
                    line.contains("Notes", ignoreCase = true) || line.contains("Бележки", ignoreCase = true) -> {
                        description = line.substringAfter(":").trim()
                    }
                }
            }

            if (destination.isEmpty() && lines.size >= 3) {
                destination = lines[0].substringAfter(":").trim()
                val datesRaw = lines[1].substringAfter(":").trim()
                val datesSplit = datesRaw.split("-")
                startDate = datesSplit.getOrNull(0)?.trim() ?: ""
                endDate = datesSplit.getOrNull(1)?.trim() ?: ""
                description = lines[2].substringAfter(":").trim()
            }

            findViewById<TextInputEditText>(R.id.editTextDestination).setText(destination)
            findViewById<TextInputEditText>(R.id.editTextStartDate).setText(startDate)
            findViewById<TextInputEditText>(R.id.editTextEndDate).setText(endDate)
            findViewById<TextInputEditText>(R.id.editTextDescription).setText(description)

        } catch (e: Exception) {
            Snackbar.make(bottomNav, "Error: ${e.message}", Snackbar.LENGTH_LONG)
                .setAnchorView(bottomNav)
                .setBackgroundTint(getColor(R.color.purple_dark))
                .show()
            e.printStackTrace()
        }
    }
}