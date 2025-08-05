package com.example.bookinglapangan.ui.QR

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookinglapangan.R
import com.example.bookinglapangan.data.session.SessionManager
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONObject

class ScanQrActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)

        requestQueue = Volley.newRequestQueue(this)

        // Panggil fungsi untuk mulai scan QR saat activity dibuka
        startQRScanner()
    }

    private fun startQRScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan QR Code Lapangan")
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                // QR Code berhasil discan
                val qrCodeData = result.contents // contoh: "LAPANGAN-3"
                sendQrDataToBackend(qrCodeData)
            } else {
                Toast.makeText(this, "Scan dibatalkan", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun sendQrDataToBackend(qrCodeData: String) {
        val sessionManager = SessionManager(this)
        val userId = sessionManager.getUserId() // Ambil user_id dari session/login

        val requestBody = JSONObject()
        requestBody.put("user_id", userId)
        requestBody.put("lapangan_id", qrCodeData)

        val request = JsonObjectRequest(
            Request.Method.POST, "https://your-backend/api/booking/confirm-entry", requestBody,
            { response ->
                // Jika sukses
                Toast.makeText(this, "Berhasil Masuk Lapangan!", Toast.LENGTH_SHORT).show()
                finish() // Kembali ke halaman sebelumnya
            },
            { error ->
                // Jika gagal
                Toast.makeText(this, "Gagal Konfirmasi: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
        requestQueue.add(request)
    }
}
