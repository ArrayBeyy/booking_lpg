package com.example.bookinglapangan.data.remote

import com.google.gson.annotations.SerializedName

data class XenditResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("external_id") val external_id: String?,
    @SerializedName("invoice_url") val invoice_url: String?
)
