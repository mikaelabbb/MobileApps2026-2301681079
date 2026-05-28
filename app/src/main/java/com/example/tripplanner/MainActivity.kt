package com.example.tripplanner

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripplanner.ui.TripAdapter
import com.example.tripplanner.viewmodel.TripViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val tripViewModel: TripViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTrips)
        val fab = findViewById<FloatingActionButton>(R.id.fabAddTrip)

        val adapter = TripAdapter(
            //opening the trip detail screen
            onItemClick = { trip ->
                val intent = Intent(this, TripDetailActivity::class.java)
                intent.putExtra("TRIP_ID", trip.id) // sending the id
                startActivity(intent)
            },
            onDeleteClick = { trip ->
                tripViewModel.delete(trip)
            }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //database
        tripViewModel.allTrips.observe(this) { trips ->
            adapter.submitList(trips)
        }

        //opening add screen
        fab.setOnClickListener {
            val intent = Intent(this, AddEditTripActivity::class.java)
            startActivity(intent)
        }
    }
}