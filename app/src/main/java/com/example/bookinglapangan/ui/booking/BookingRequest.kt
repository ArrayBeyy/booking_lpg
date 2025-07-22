package com.example.bookinglapangan.ui.booking

data class BookingRequest(
    val lapangan_id: Int,
    val tanggal: String,
    val jam_mulai: String,
    val jam_selesai: String,
    val status: String = "dp",
    val metode_pembayaran: String? = null,
    val repeat_weekly: Boolean = false
)

