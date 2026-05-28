package com.example.tripplanner

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tripplanner.data.Trip
import com.example.tripplanner.viewmodel.TripViewModel
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class TripDetailActivity : AppCompatActivity() {

    private val tripViewModel: TripViewModel by viewModels()
    private var currentTrip: Trip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_detail)

        //creating the UI elements
        val tvDestination = findViewById<TextView>(R.id.detailDestination)
        val tvDates = findViewById<TextView>(R.id.detailDates)
        val tvDescription = findViewById<TextView>(R.id.detailDescription)
        val ivQRCode = findViewById<ImageView>(R.id.imageViewQRCode)
        val btnShare = findViewById<Button>(R.id.buttonShareTrip)

        //taking the id of the trip
        val tripId = intent.getIntExtra("TRIP_ID", -1)

        if (tripId != -1) {
            //finding the trip by ID
            tripViewModel.allTrips.observe(this) { trips ->
                currentTrip = trips.find { it.id == tripId }

                currentTrip?.let { trip ->
                    tvDestination.text = trip.destination
                    tvDates.text = "Duration: ${trip.startDate} - ${trip.endDate}"
                    tvDescription.text = trip.description

                    //generating a QR code
                    try {
                        val qrData = "Trip to: ${trip.destination}\nDates: ${trip.startDate}-${trip.endDate}\nNotes: ${trip.description}"
                        val barcodeEncoder = BarcodeEncoder()
                        val bitmap: Bitmap = barcodeEncoder.encodeBitmap(qrData, BarcodeFormat.QR_CODE, 400, 400)
                        ivQRCode.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Error loading the data!", Toast.LENGTH_SHORT).show()
            finish()
        }

        //share intent
        btnShare.setOnClickListener {
            currentTrip?.let { trip ->
                val shareText = """
                    Planned a new trip!
                    Destination: ${trip.destination}
                    Date: ${trip.startDate} - ${trip.endDate}
                    Notes: ${trip.description}
                """.trimIndent()

                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, "Share the trip:")
                startActivity(shareIntent)
            }
        }
    }
}