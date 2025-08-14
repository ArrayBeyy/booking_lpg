package com.example.bookinglapangan.data.api

import android.content.Context
import com.example.bookinglapangan.BookingApp
import com.google.gson.FieldNamingPolicy                     // ⬅️ tambah
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // ⬇️ gunakan HTTPS dan pastikan ada trailing slash
    private const val BASE_URL = "https://book.deltagrup.id/api/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val acceptJsonInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .build()
        chain.proceed(request)
    }

    private val tokenInterceptor = Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        val context = BookingApp.context
        val sharedPreferences = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)
        token?.let { requestBuilder.addHeader("Authorization", "Bearer $it") }
        chain.proceed(requestBuilder.build())
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(acceptJsonInterceptor)
        .addInterceptor(tokenInterceptor)
        .build()

    // ⬇️ PENTING: mapping snake_case JSON → camelCase properti Kotlin
    private val gson = GsonBuilder()
        .setLenient()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}
