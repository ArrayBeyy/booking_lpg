package com.example.bookinglapangan.ui.payment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookinglapangan.data.api.RetrofitClient
import com.example.bookinglapangan.data.remote.StatusRes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentViewModel : ViewModel() {

    data class UiState(
        val loading: Boolean = false,
        val error: String? = null,
        val invoiceUrl: String? = null,
        val externalId: String? = null,
        val status: String? = null
    )

    // One-shot event untuk minta UI buka halaman invoice
    data class OpenInvoice(val url: String, val externalId: String)

    private val _openInvoice = MutableSharedFlow<OpenInvoice>(extraBufferCapacity = 1)
    val openInvoice = _openInvoice.asSharedFlow()

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    fun createPayment(email: String, description: String, amount: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)

            try {
                // Kirim sebagai Map agar cocok dengan ApiService yang expect Map<String, Any>
                val req = mapOf(
                    "email" to email,
                    "description" to description,
                    "amount" to amount // atau amount.toLong() jika backend minta long
                    // tambahkan "lapangan_id" dsb jika dibutuhkan backend
                )

                // ⬇️ ApiService mengembalikan XenditResponse LANGSUNG (bukan Response<>)
                val body = RetrofitClient.apiService.createPayment(req)

                // Ambil url & externalId secara defensif (camelCase/snake_case)
                val url = body.readStringCompat(
                    "invoiceUrl", "invoice_url", "invoiceURL", "invoice", "url"
                )
                val ext = body.readStringCompat(
                    "externalId", "external_id", "externalID", "ext", "reference_id"
                )

                if (!url.isNullOrBlank() && !ext.isNullOrBlank()) {
                    Log.d("PAY", "VM received: url=$url, ext=$ext")

                    _state.value = _state.value.copy(
                        loading = false,
                        invoiceUrl = url,
                        externalId = ext,
                        status = null
                    )

                    // trigger UI untuk buka WebView
                    _openInvoice.tryEmit(OpenInvoice(url, ext))
                } else {
                    _state.value = _state.value.copy(
                        loading = false,
                        error = "invoice_url / external_id kosong dari server"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message ?: "Terjadi kesalahan tak terduga"
                )
            }
        }
    }

    fun pollStatus(intervalMs: Long = 4000L, maxTries: Int = 20) {
        val ext = _state.value.externalId ?: return
        viewModelScope.launch {
            var tries = 0
            while (tries < maxTries) {
                try {
                    val res = RetrofitClient.apiService.getPaymentStatus(ext)
                    if (res.isSuccessful) {
                        val body: StatusRes? = res.body()
                        if (body != null) {
                            _state.value = _state.value.copy(status = body.status)
                            if (body.status == "PAID" || body.status == "EXPIRED") {
                                break
                            }
                        }
                    } else {
                        if (_state.value.error == null) {
                            _state.value = _state.value.copy(
                                error = "Gagal cek status (${res.code()})"
                            )
                        }
                    }
                } catch (e: Exception) {
                    if (_state.value.error == null) {
                        _state.value = _state.value.copy(error = e.message)
                    }
                }
                tries++
                delay(intervalMs)
            }
        }
    }

    fun reset() {
        _state.value = UiState()
    }
}

/* ===================== Helpers ===================== */

/**
 * Baca properti String dari objek respons dengan beberapa kemungkinan nama field
 * (mendukung camelCase dan snake_case), tanpa memaksa tipe model tertentu.
 *
 * Contoh: readStringCompat("invoiceUrl", "invoice_url")
 */
private fun Any.readStringCompat(vararg names: String): String? {
    // 1) Coba via getter (getInvoiceUrl, getExternalId, dll)
    for (n in names) {
        val getterName = "get" + n.replaceFirstChar { it.uppercase() }
            .replace("_", "")
        runCatching {
            val m = this.javaClass.methods.firstOrNull { it.name.equals(getterName, ignoreCase = true) }
            val v = m?.invoke(this) as? String
            if (!v.isNullOrBlank()) return v
        }
    }
    // 2) Coba via field langsung (invoiceUrl, invoice_url, dll)
    for (n in names) {
        runCatching {
            val f = this.javaClass.getDeclaredField(n)
            f.isAccessible = true
            val v = f.get(this) as? String
            if (!v.isNullOrBlank()) return v
        }
    }
    return null
}
