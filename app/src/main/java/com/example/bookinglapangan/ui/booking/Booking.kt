package com.example.bookinglapangan.ui.booking

import androidx.room.Entity
import androidx.room.PrimaryKey

// TIDAK diberi anotasi @Entity
data class Booking(
    val id: Int,
    val user_id: Int,
    val lapangan_id: Int,
    val tanggal: String,
    val jam_mulai: String,
    val jam_selesai: String,
    val status: String,
    val metode_pembayaran: String?,
    val repeat_weekly: Boolean,
)

data class Lapangan(
    val id: Int,
    val nama: String
)

