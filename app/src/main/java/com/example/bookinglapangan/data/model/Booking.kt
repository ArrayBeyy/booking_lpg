package com.example.bookinglapangan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val waktu: String,
    val lapangan: String,
    val loyaltyPoint: Int = 0
)

