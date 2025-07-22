package com.example.bookinglapangan.ui.booking

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
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
        }
    }
}
