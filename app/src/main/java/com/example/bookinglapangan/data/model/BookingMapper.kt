package com.example.bookinglapangan.model

import com.example.bookinglapangan.ui.booking.Booking
import com.example.bookinglapangan.ui.booking.BookingDisplay

fun Booking.toDisplay(): BookingDisplay {
    return BookingDisplay(
        lapangan = this.lapangan_nama,
        tanggal = this.tanggal,
        waktu = "${this.jam_mulai} - ${this.jam_selesai}"
    )
}
