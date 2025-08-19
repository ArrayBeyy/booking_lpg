// com/example/bookinglapangan/data/remote/XenditResponse.kt
package com.example.bookinglapangan.data.remote

import com.google.gson.annotations.SerializedName

data class XenditResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("external_id") val externalId: String?,   // HANYA INI, JANGAN ADA YG LAIN utk external_id
    @SerializedName("user_id") val userId: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("amount") val amount: Long?,
    @SerializedName("payer_email") val payerEmail: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("invoice_url") val invoiceUrl: String?,
    @SerializedName("expiry_date") val expiryDate: String?,
    // tambahkan field lain sesuai kebutuhan, semuanya optional aman kalau backend tidak kirim
)
