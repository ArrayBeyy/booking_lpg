package com.example.bookinglapangan.data.api

import com.example.bookinglapangan.data.model.LapanganDetailDto
import com.example.bookinglapangan.data.remote.StatusRes
import com.example.bookinglapangan.data.remote.XenditResponse
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
import retrofit2.http.Query

// ====== ALIAS: model lama (dipakai screen legacy, biarkan) ======
import com.example.bookinglapangan.ui.Lapangan.LapanganResponse as LapanganResponseOld

interface ApiService {

    // --- Auth ---
    @POST("mobile/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // --- Lapangan (LEGACY) ---
    // Tetap dipertahankan untuk UI lama yang masih konsumsi respons ini.
    @GET("lapangan")
    suspend fun getLapangans(): Response<LapanganResponseOld>

    // ==========================
    // Lapangan (BARU / JSON)
    // ==========================

    // Generic list (JSON) → /api/lapangan?q=&kategori=&kategori_id=&per_page=
    // (Laravel: LapanganController@indexJson)
    @GET("lapangan")
    suspend fun getLapanganJson(
        @Query("q") q: String? = null,
        @Query("kategori") kategoriSlug: String? = null,
        @Query("kategori_id") kategoriId: Long? = null,
        @Query("per_page") perPage: Int = 12
    ): Response<com.example.bookinglapangan.data.model.LapanganResponse>

    // Filter by kategori slug (pakai query `kategori=<slug>`)
    @GET("lapangan")
    suspend fun getLapanganByKategori(
        @Query("kategori") kategoriSlug: String,
        @Query("q") q: String? = null,
        @Query("per_page") perPage: Int = 12
    ): Response<com.example.bookinglapangan.data.model.LapanganResponse>

    // Filter by kategori ID (pakai query `kategori_id=<id>`)
    @GET("lapangan")
    suspend fun getLapanganByKategoriId(
        @Query("kategori_id") kategoriId: Int,
        @Query("q") q: String? = null,
        @Query("per_page") perPage: Int = 12
    ): Response<com.example.bookinglapangan.data.model.LapanganResponse>

    // Detail (JSON) → /api/lapangan/{id} (Laravel: showJson)
    @GET("lapangan/{id}")
    suspend fun getLapanganDetail(@Path("id") id: Int): LapanganDetailDto

    // --- Booking ---
    // NOTE: Biarkan path "booking" jika backend kamu memang ada route itu.
    // Jika backend pakai plural (/bookings, auth:sanctum), ganti sesuai kebutuhan.
    @POST("booking")
    suspend fun createBooking(@Body booking: BookingRequest): Response<ApiResponse>
    // @POST("bookings")
    // suspend fun createBooking(@Body booking: BookingRequest): Response<ApiResponse>

    // --- Upload bukti ---
    @Multipart
    @POST("upload-bukti")
    fun uploadBukti(
        @Part("nama") nama: RequestBody,
        @Part("cabang") cabang: RequestBody,
        @Part bukti_bayar: MultipartBody.Part
    ): Call<ApiResponse>

    // --- Xendit ---
    @POST("xendit/create-payment")
    suspend fun createPayment(@Body body: Map<String, @JvmSuppressWildcards Any>): XenditResponse

    @GET("xendit/status/{external_id}")
    suspend fun getPaymentStatus(@Path("external_id") externalId: String): Response<StatusRes>
}
