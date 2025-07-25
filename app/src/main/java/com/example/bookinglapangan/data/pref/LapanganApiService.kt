package com.example.bookinglapangan.data.pref

import retrofit2.http.GET

interface LapanganApiService {
    @GET("api/lapangan") // ganti sesuai endpoint Laravel kamu
    suspend fun getLapangan(): List<Lapangan>
}
