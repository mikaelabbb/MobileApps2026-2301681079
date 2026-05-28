package com.example.tripplanner.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.tripplanner.data.Trip
import com.example.tripplanner.data.TripDatabase
import com.example.tripplanner.repository.TripRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TripViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TripRepository

    // LiveData, list for all of the trips
    val allTrips: LiveData<List<Trip>>

    init {
        // initializing DAO, database and repo
        val tripDao = TripDatabase.getDatabase(application).tripDao()
        repository = TripRepository(tripDao)

        // turning flow to livedata
        allTrips = repository.allTrips.asLiveData()
    }

    //crud operations as a background processes

    //create
    fun insert(trip: Trip) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(trip)
    }

    //update
    fun update(trip: Trip) = viewModelScope.launch {
        repository.update(trip)
    }

    //delete
    fun delete(trip: Trip) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(trip)
    }
}