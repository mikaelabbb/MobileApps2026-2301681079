package com.example.tripplanner

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripplanner.ui.TripAdapter
import com.example.tripplanner.viewmodel.TripViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {

    private val tripViewModel: TripViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        var keepSplash = true

        Handler(Looper.getMainLooper()).postDelayed({
            keepSplash = false
        }, 1500)

        splashScreen.setKeepOnScreenCondition { keepSplash }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //bottom nav
        val bottomNav = findViewById<BottomNavigationView>(
            R.id.bottomNavigation
        )

        BottomNavHelper.setup(bottomNav, this)

        bottomNav.selectedItemId = R.id.nav_trips

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTrips)

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

    }
}