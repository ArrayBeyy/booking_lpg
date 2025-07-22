package com.example.bookinglapangan.ui.booking

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bookinglapangan.data.api.RetrofitClient
import com.example.bookinglapangan.databinding.ActivityBookingBinding
import kotlinx.coroutines.launch

class BookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBooking.setOnClickListener {
            val lapanganId = binding.etLapanganId.text.toString().toIntOrNull()
            val tanggal = binding.etTanggal.text.toString()
            val jamMulai = binding.etJamMulai.text.toString()
            val jamSelesai = binding.etJamSelesai.text.toString()
            val metode = binding.etMetode.text.toString()
            val repeat = binding.cbRepeat.isChecked
            val status = if (binding.radioFull.isChecked) "full" else "dp"

            if (lapanganId == null || tanggal.isEmpty() || jamMulai.isEmpty() || jamSelesai.isEmpty()) {
                Toast.makeText(this, "Lengkapi semua data terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bookingRequest = BookingRequest(
                lapangan_id = lapanganId,
                tanggal = tanggal,
                jam_mulai = jamMulai,
                jam_selesai = jamSelesai,
                status = status,
                metode_pembayaran = metode,
                repeat_weekly = repeat
            )

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.createBooking(bookingRequest)
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@BookingActivity,
                            response.body()?.message ?: "Booking berhasil",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this@BookingActivity, "Gagal booking", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@BookingActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
