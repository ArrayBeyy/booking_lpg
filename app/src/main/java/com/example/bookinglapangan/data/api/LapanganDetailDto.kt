package com.example.bookinglapangan.data.api

data class LapanganDetailDto(
    val id: Int,
    val nama: String,
    val kategori: String,
    val lokasi: String,
    val harga_per_jam: Int,
    val jumlah_lapangan: Int,
    val buka: String,
    val tutup: String,
    val fasilitas: List<String> = emptyList(),
    val cover_image: String? = null,
    val rating: Float = 0f,
    val deskripsi: String? = null,
    // optional jika backend kirim koordinat dll
    val kota: String? = null,
    val foto: List<String>? = null
)