package com.example.tripplanner

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tripplanner.data.Trip
import com.example.tripplanner.viewmodel.TripViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class TripDetailActivity : AppCompatActivity() {

    private val tripViewModel: TripViewModel by viewModels()
    private var currentTrip: Trip? = null

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.applySettings(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_detail)

        val bottomNav = findViewById<BottomNavigationView>(
            R.id.bottomNavigation
        )

        BottomNavHelper.setup(bottomNav, this)

        //creating the UI elements
        val tvDestination = findViewById<TextView>(R.id.detailDestination)
        val tvDates = findViewById<TextView>(R.id.detailDates)
        val tvDescription = findViewById<TextView>(R.id.detailDescription)
        val ivQRCode = findViewById<ImageView>(R.id.imageViewQRCode)
        val btnShare = findViewById<Button>(R.id.buttonShareTrip)
        val btnEdit = findViewById<Button>(R.id.buttonEditTrip)

        //taking the id of the trip
        val tripId = intent.getIntExtra("TRIP_ID", -1)

        if (tripId != -1) {
            //finding the trip by ID
            tripViewModel.allTrips.observe(this) { trips ->
                currentTrip = trips.find { it.id == tripId }

                currentTrip?.let { trip ->
                    tvDestination.text = trip.destination
                    tvDates.text = getString(R.string.duration_label, trip.startDate, trip.endDate)
                    tvDescription.text = trip.description

                    //generating a QR code
                    try {
                        val qrData = "${getString(R.string.qr_destination, trip.destination)}\n" +
                                "${getString(R.string.qr_dates, trip.startDate, trip.endDate)}\n" +
                                getString(R.string.qr_notes, trip.description)
                        val barcodeEncoder = BarcodeEncoder()
                        val bitmap: Bitmap = barcodeEncoder.encodeBitmap(qrData, BarcodeFormat.QR_CODE, 400, 400)
                        ivQRCode.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        // Модерен Snackbar при проблем с QR кода
                        Snackbar.make(bottomNav, "Error generating QR Code", Snackbar.LENGTH_LONG)
                            .setAnchorView(bottomNav)
                            .setBackgroundTint(getColor(R.color.purple_dark))
                            .show()
                        e.printStackTrace()
                    }
                }
            }
        } else {
            // КОРЕКЦИЯ: Snackbar за грешка при зареждане с изчакване преди finish()
            Snackbar.make(bottomNav, getString(R.string.error_loading_data), Snackbar.LENGTH_SHORT)
                .setAnchorView(bottomNav)
                .setBackgroundTint(getColor(R.color.purple_dark))
                .show()

            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 400)
        }

        //share intent
        btnShare.setOnClickListener {
            currentTrip?.let { trip ->
                val shareText = """
                    ${getString(R.string.share_headline)}
                    ${getString(R.string.share_destination, trip.destination)}
                    ${getString(R.string.share_date, trip.startDate, trip.endDate)}
                    ${getString(R.string.share_notes, trip.description)}
                """.trimIndent()

                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, getString(R.string.share_title))
                startActivity(shareIntent)
            }
        }

        btnEdit.setOnClickListener {
            currentTrip?.let { trip ->
                val intent = Intent(
                    this,
                    AddEditTripActivity::class.java
                )
                intent.putExtra("TRIP_ID", trip.id)
                startActivity(intent)
            }
        }
    }
}