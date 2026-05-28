package com.example.tripplanner.repository

import com.example.tripplanner.data.Trip
import com.example.tripplanner.data.TripDao
import kotlinx.coroutines.flow.Flow

class TripRepository(private val tripDao: TripDao) {

    //all trips (Flow)
    val allTrips: Flow<List<Trip>> = tripDao.getAllTrips()

    //adding
    suspend fun insert(trip: Trip) {
        tripDao.insertTrip(trip)
    }

    //updating
    suspend fun update(trip: Trip) {
        tripDao.update(trip)
    }

    //deleting
    suspend fun delete(trip: Trip) {
        tripDao.deleteTrip(trip)
    }
}