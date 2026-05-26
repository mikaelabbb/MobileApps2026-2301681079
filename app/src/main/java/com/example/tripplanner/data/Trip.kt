package com.example.tripplanner.data // Увери се, че това съвпада с твоя пакет

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val description: String,
    val qrCodeData: String? = null
)