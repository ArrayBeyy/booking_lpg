package com.example.bookinglapangan.data.remote

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class CreatePaymentReq(
    @Json(name = "email")       val email: String,
    @Json(name = "description") val description: String,
    @Json(name = "amount")      val amount: Long,
    @Json(name = "external_id") val externalId: String? = null
)