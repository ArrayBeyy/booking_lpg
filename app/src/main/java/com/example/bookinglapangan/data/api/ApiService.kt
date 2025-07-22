package com.example.bookinglapangan.data.api

import com.example.bookinglapangan.ui.Lapangan.Lapangan
import com.example.bookinglapangan.ui.booking.BookingHistoryResponse
import com.example.bookinglapangan.ui.booking.BookingRequest
import com.example.bookinglapangan.ui.login.LoginRequest
import com.example.bookinglapangan.ui.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("mobile-login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    /*@POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>*/

    @GET("lapangans")
    suspend fun getLapangans(): Response<List<Lapangan>>

    @POST("booking")
    suspend fun createBooking(@Body booking: BookingRequest): Response<ApiResponse>

    @GET("bookings/history")
    suspend fun getBookingHistory(): Response<BookingHistoryResponse>

    /*@GET("bookings/all")
    suspend fun getAllBookings(): Response<BookingHistoryResponse>*/
}
