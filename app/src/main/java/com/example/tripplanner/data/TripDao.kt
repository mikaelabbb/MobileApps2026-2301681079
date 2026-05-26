package com.example.tripplanner.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    //add new trip
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: Trip)

    //read (sort by start date)
    @Query("SELECT * FROM trips ORDER BY startDate ASC")
    fun getAllTrips(): Flow<List<Trip>>

    //update trip
    @Update
    suspend fun updateTrip(trip: Trip)

    //delete trip
    @Delete
    suspend fun deleteTrip(trip: Trip)
}