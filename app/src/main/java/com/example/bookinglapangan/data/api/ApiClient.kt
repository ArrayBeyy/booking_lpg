package com.example.bookinglapangan.data.api

import com.example.bookinglapangan.data.pref.LapanganApiService

object ApiClient {
    private const val BASE_URL = "http://192.168.1.10:8000/" // ganti IP Laravel kamu

    val lapanganService: LapanganApiService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(LapanganApiService::class.java)
    }
}
