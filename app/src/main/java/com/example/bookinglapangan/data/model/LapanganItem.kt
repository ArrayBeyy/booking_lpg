package com.example.bookinglapangan.data.model

@kotlinx.parcelize.Parcelize
data class LapanganItem(
    val id: Int,
    val nama: String,
    val kota: String? = null,
    val harga_per_jam: Int = 0,
    val rating: Float? = null,
    val jumlah_lapangan: Int? = null,
    val deskripsi: String? = null,
    val alamat: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val foto: List<String> = emptyList(),
    val fasilitas: List<String> = emptyList(),
    val jam_operasional: JamOperasional? = null
) : android.os.Parcelable

