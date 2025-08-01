package com.example.bookinglapangan.ui.booking

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.bookinglapangan.data.local.BookingEntity
import com.example.bookinglapangan.data.model.AppDatabase
import kotlinx.coroutines.launch

class BookingViewModel(application: Application) : AndroidViewModel(application) {

    // DAO dari database lokal Room
    private val bookingDao = AppDatabase.getDatabase(application).bookingDao()

    // LiveData untuk semua data booking
    val allBookings: LiveData<List<BookingEntity>> = bookingDao.getAllBookings()

    // Fungsi insert (versi lengkap)
    fun insertBooking(
        lapanganId: Int,
        tanggal: String,
        jamMulai: String,
        jamSelesai: String,
        status: String = "full",
        metodePembayaran: String? = null,
        repeatWeekly: Boolean = false
    ) {
        viewModelScope.launch {
            val booking = BookingEntity(
                id = 0,
                lapangan_id = lapanganId,
                tanggal = tanggal,
                jam_mulai = jamMulai,
                jam_selesai = jamSelesai,
                status = status,
                metode_pembayaran = metodePembayaran,
                repeat_weekly = repeatWeekly
            )
            bookingDao.insert(booking)
        }
    }

    // Fungsi insert (versi sederhana)
    fun insertBooking(nama: String, waktu: String, lapangan: String) {
        viewModelScope.launch {
            val booking = BookingEntity(
                id = 0,
                lapangan_id = 0, // jika tidak tersedia, default 0
                tanggal = "",
                jam_mulai = waktu,
                jam_selesai = "",
                status = "dp",
                metode_pembayaran = null,
                repeat_weekly = false,
            )
            bookingDao.insert(booking)
        }
    }
}
