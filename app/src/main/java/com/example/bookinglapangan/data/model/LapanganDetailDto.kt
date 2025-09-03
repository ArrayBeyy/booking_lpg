package com.example.bookinglapangan.data.model

// data/api/models.kt
import com.google.gson.annotations.SerializedName

data class LapanganDetailDto(
    val id: Int,
    val nama: String,
    val kategori: String,                         // contoh: "Badminton"
    val lokasi: String,                           // alamat
    @SerializedName("harga_per_jam") val hargaPerJam: Int,
    @SerializedName("jumlah_lapangan") val jumlahLapangan: Int,
    // jam bisa "06:00" atau "06:00:00" → kita handle di Activity
    val buka: String,
    val tutup: String,
    // fasilitas dari API: array string (["Parkir","AC","Wifi",...])
    val fasilitas: List<String> = emptyList(),
    @SerializedName("cover_image") val coverImage: String?,
    val rating: Float = 0f,
    val deskripsi: String? = null
)
