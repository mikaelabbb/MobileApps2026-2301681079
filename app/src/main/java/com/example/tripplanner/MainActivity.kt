package com.example.tripplanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
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
        bottomNav.selectedItemId = R.id.nav_all_trips

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

        //finding the textview for the empty list
        val emptyText = findViewById<TextView>(R.id.emptyText)

        //observer for the database
        tripViewModel.allTrips.observe(this) { trips ->
            adapter.submitList(trips)

            //checks if there are any trips
            if (trips.isEmpty()) {
                emptyText.visibility = View.VISIBLE    //shows the text
                recyclerView.visibility = View.GONE   //hides the list
            } else {
                emptyText.visibility = View.GONE      //hides the text
                recyclerView.visibility = View.VISIBLE //shows the list
            }
        }
    }
}