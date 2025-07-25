package com.example.bookinglapangan.ui.booking

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
<<<<<<< HEAD
import com.example.bookinglapangan.data.local.BookingEntity
import com.example.bookinglapangan.data.model.AppDatabase
import kotlinx.coroutines.launch

class BookingViewModel(application: Application) : AndroidViewModel(application) {

    // Ambil instance DAO dari database lokal Room
    private val bookingDao = AppDatabase.getDatabase(application).bookingDao()

    // LiveData untuk mengamati seluruh data booking
    val allBookings: LiveData<List<BookingEntity>> = bookingDao.getAllBookings()

    // Fungsi untuk menyimpan booking ke database lokal
    fun insertBooking(
        lapanganId: Int,
        tanggal: String,
        jamMulai: String,
        jamSelesai: String,
        status: String = "dp",
        metodePembayaran: String? = null,
        repeatWeekly: Boolean = false
    ) {
        viewModelScope.launch {
            val booking = BookingEntity(
                id = 0, // ID auto-generate oleh Room
                lapangan_id = lapanganId,
                tanggal = tanggal,
                jam_mulai = jamMulai,
                jam_selesai = jamSelesai,
                status = status,
                metode_pembayaran = metodePembayaran,
                repeat_weekly = repeatWeekly
            )
            bookingDao.insert(booking)
=======
import com.example.bookinglapangan.data.model.AppDatabase
import com.example.bookinglapangan.data.model.Booking
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData

class BookingViewModel(application: Application) : AndroidViewModel(application) {
    private val bookingDao = AppDatabase.getDatabase(application).bookingDao()
    val allBookings: LiveData<List<Booking>> = bookingDao.getAll().asLiveData()

    fun insertBooking(nama: String, waktu: String, lapangan: String) {
        viewModelScope.launch {
            bookingDao.insert(Booking(nama = nama, waktu = waktu, lapangan = lapangan))
>>>>>>> 0f717ec40a751538100d5e67f9067b42546afb5e
        }
    }
}
