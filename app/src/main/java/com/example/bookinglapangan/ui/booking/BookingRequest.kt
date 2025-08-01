package com.example.bookinglapangan.ui.booking

data class BookingRequest(
    val user_id: Int,
    val lapangan_id: Int,
    val tanggal: String,
    val jam_mulai: String,
    val jam_selesai: String,
    val status: String = "full",
    val metode_pembayaran: String? = null,
    val repeat_weekly: Boolean = false
)

