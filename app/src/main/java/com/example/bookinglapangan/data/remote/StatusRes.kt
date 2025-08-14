package com.example.bookinglapangan.data.remote

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class StatusRes(
    @Json(name = "external_id")  val externalId: String,
    @Json(name = "invoice_id")   val invoiceId: String?,
    @Json(name = "status")       val status: String,
    @Json(name = "invoice_url")  val invoiceUrl: String?,
    @Json(name = "amount")       val amount: Long?,
    @Json(name = "description")  val description: String?
)
