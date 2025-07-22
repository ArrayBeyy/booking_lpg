package com.example.bookinglapangan.ui.booking

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bookinglapangan.data.local.BookingEntity

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY tanggal DESC")
    fun getAllBookings(): LiveData<List<BookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(booking: BookingEntity)
}
