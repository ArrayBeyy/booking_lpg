package com.example.bookinglapangan.data.api

import com.example.bookinglapangan.ui.Lapangan.Lapangan
import com.example.bookinglapangan.ui.Lapangan.LapanganResponse
import com.example.bookinglapangan.ui.booking.BookingHistoryResponse
import com.example.bookinglapangan.ui.booking.BookingRequest
import com.example.bookinglapangan.ui.login.LoginRequest
import com.example.bookinglapangan.ui.login.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("mobile/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("lapangan")
    suspend fun getLapangans(): Response<LapanganResponse>

    @POST("booking")
    suspend fun createBooking(@Body booking: BookingRequest): Response<ApiResponse>

    /*@GET("bookings/history")
    suspend fun getBookingHistory(): Response<BookingHistoryResponse>

    @GET("bookings/all")
    suspend fun getAllBookings(): Response<BookingHistoryResponse>*/

    @Multipart
    @POST("upload-bukti")
    fun uploadBukti(
        @Part("nama") nama: RequestBody,
        @Part("cabang") cabang: RequestBody,
        @Part bukti_bayar: MultipartBody.Part
    ): Call<ApiResponse>

}
