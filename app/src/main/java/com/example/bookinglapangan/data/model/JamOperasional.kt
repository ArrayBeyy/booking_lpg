package com.example.bookinglapangan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JamOperasional(
    val senin: List<String> = emptyList(),
    val selasa: List<String> = emptyList(),
    val rabu: List<String> = emptyList(),
    val kamis: List<String> = emptyList(),
    val jumat: List<String> = emptyList(),
    val sabtu: List<String> = emptyList(),
    val minggu: List<String> = emptyList()
) : Parcelable
