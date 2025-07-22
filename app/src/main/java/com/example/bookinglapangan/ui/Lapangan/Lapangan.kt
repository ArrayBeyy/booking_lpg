package com.example.bookinglapangan.ui.Lapangan

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lapangan")
data class Lapangan(
    @PrimaryKey val id: Int,
    val nama: String
)


