package com.example.bookinglapangan.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lapangan_id: Int,
    val tanggal: String,
    val jam_mulai: String,
    val jam_selesai: String,
    val status: String,
    val metode_pembayaran: String?,
    val repeat_weekly: Boolean
)
