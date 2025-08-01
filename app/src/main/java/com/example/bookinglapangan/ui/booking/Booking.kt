package com.example.bookinglapangan.ui.booking

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,
    val id: Int = 0,
    val user_id: Int,
    val lapangan_id: Int,
    val tanggal: String,
    val jam_mulai: String,
    val jam_selesai: String,
    val status: String,
    val metode_pembayaran: String? = null,
    val repeat_weekly: Boolean = false,
    val lapangan_nama: String = ""
)


