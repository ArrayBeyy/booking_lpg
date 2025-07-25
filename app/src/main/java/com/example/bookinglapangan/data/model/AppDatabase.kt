package com.example.bookinglapangan.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
<<<<<<< HEAD
import com.example.bookinglapangan.data.local.BookingEntity
import com.example.bookinglapangan.ui.booking.BookingDao

@Database(entities = [BookingEntity::class], version = 1)
=======

@Database(entities = [Booking::class], version = 1)
>>>>>>> 0f717ec40a751538100d5e67f9067b42546afb5e
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
