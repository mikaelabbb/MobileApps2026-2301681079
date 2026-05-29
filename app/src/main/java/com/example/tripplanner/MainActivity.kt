package com.example.tripplanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripplanner.ui.TripAdapter
import com.example.tripplanner.viewmodel.TripViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private val tripViewModel: TripViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.applySettings(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        //splash screen
        val splashScreen = installSplashScreen()

        var keepSplash = true
        Handler(Looper.getMainLooper()).postDelayed({
            keepSplash = false
        }, 1500)

        splashScreen.setKeepOnScreenCondition { keepSplash }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //bottom nav setup
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        BottomNavHelper.setup(bottomNav, this)
        bottomNav.selectedItemId = R.id.nav_trips

        //recycler view setup
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTrips)
        val adapter = TripAdapter(
            onItemClick = { trip ->
                val intent = Intent(this, TripDetailActivity::class.java)
                intent.putExtra("TRIP_ID", trip.id)
                startActivity(intent)
            },
            onDeleteClick = { trip ->
                //dialog
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.delete_dialog_title))
                    .setMessage(getString(R.string.delete_dialog_message))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.delete_dialog_cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(getString(R.string.delete_dialog_confirm)) { dialog, _ ->
                        tripViewModel.delete(trip) //deleting only if the user clicks on "Delete"
                        dialog.dismiss()
                    }
                    .show()
            }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // database observer
        tripViewModel.allTrips.observe(this) { trips ->
            adapter.submitList(trips)
        }
    }
}