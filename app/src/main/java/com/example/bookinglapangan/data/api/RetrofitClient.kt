package com.example.bookinglapangan.data.api

import android.content.Context
import com.example.bookinglapangan.BookingApp
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://book.deltagrup.id/api/"

    // Logging Interceptor
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Header Interceptor untuk Accept: application/json
    private val acceptJsonInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json") // Tambahkan header ini
            .build()
        chain.proceed(request)
    }

    // Token Interceptor
    private val tokenInterceptor = Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        val context = BookingApp.context
        val sharedPreferences = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        chain.proceed(requestBuilder.build())
    }

    // OkHttpClient lengkap
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)              // Log dulu
        .addInterceptor(acceptJsonInterceptor) // Tambahkan Accept: application/json
        .addInterceptor(tokenInterceptor)     // Tambahkan token jika ada
        .build()

    // Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            ))
            .build()
    }

    // ApiService instance
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
