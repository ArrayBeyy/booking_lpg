package com.example.bookinglapangan.ui.booking

data class BookingHistoryResponse(
    val status: String,
    val data: List<BookingData>
)

data class BookingData(
    val id: Int,
    val lapangan_id: Int,
    val user_id: Int,
    val tanggal: String,
    val jam_mulai: String,
    val jam_selesai: String,
    val status: String,
    val created_at: String?,
    val updated_at: String?
)

