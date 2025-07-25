package com.example.bookinglapangan.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Insert
    suspend fun insert(booking: Booking)

    @Query("SELECT * FROM bookings ORDER BY id DESC")
    fun getAll(): Flow<List<Booking>>
}
