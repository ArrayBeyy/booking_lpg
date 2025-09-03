package com.example.bookinglapangan.data.repository

import com.example.bookinglapangan.data.api.ApiService
import com.example.bookinglapangan.data.model.LapanganDetailDto
import retrofit2.Response

class LapanganRepository(
    private val api: ApiService
) {

    // Helper: ambil body() atau lempar error dengan pesan rapi
    private inline fun <reified T> Response<T>.requireBody(opName: String): T {
        if (isSuccessful) {
            val b = body()
            if (b != null) return b
        }
        val msg = errorBody()?.string().orEmpty().ifBlank { message().orEmpty() }
        throw IllegalStateException("$opName gagal: ${code()} $msg")
    }

    // =========================
    // 1) ENDPOINT LAMA (legacy)
    // =========================
    suspend fun fetchAllLegacy(): com.example.bookinglapangan.ui.Lapangan.LapanganResponse {
        val res: Response<com.example.bookinglapangan.ui.Lapangan.LapanganResponse> =
            api.getLapangans()
        return res.requireBody("Memuat semua lapangan (legacy)")
    }

    // ==================================
    // 2) Baru: by kategori slug (wrapper { data: [...] })
    // ==================================
    suspend fun fetchByKategori(
        slug: String,
        q: String? = null
    ): List<com.example.bookinglapangan.data.model.LapanganItem> {

        val res: Response<com.example.bookinglapangan.data.model.LapanganResponse> =
            api.getLapanganByKategori(slug, q)

        val body: com.example.bookinglapangan.data.model.LapanganResponse =
            res.requireBody("Memuat lapangan by kategori")

        return body.data
    }

    // ==================================
    // 3) Baru: by kategori ID (wrapper { data: [...] })
    // ==================================
    suspend fun fetchByKategoriId(
        kategoriId: Int,
        q: String? = null
    ): List<com.example.bookinglapangan.data.model.LapanganItem> {

        val res: Response<com.example.bookinglapangan.data.model.LapanganResponse> =
            api.getLapanganByKategoriId(kategoriId, q)

        val body: com.example.bookinglapangan.data.model.LapanganResponse =
            res.requireBody("Memuat lapangan by kategoriId")

        return body.data
    }

    // ==================================
    // 4) DETAIL: 1 endpoint (langsung object, TANPA Response)
    // ==================================
    suspend fun fetchDetail(id: Int): LapanganDetailDto {
        // ApiService.getLapanganDetail() harus ber-signature:
        // suspend fun getLapanganDetail(@Path("id") id: Int): LapanganDetailDto
        return api.getLapanganDetail(id)
    }
}
