package com.example.bookinglapangan.ui.booking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bookinglapangan.data.api.RetrofitClient
import com.example.bookinglapangan.databinding.ActivityBookingBinding
import com.example.bookinglapangan.ui.Lapangan.LapanganViewModel
import com.example.bookinglapangan.ui.payment.ManualPaymentActivity
import kotlinx.coroutines.launch

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private lateinit var lapanganViewModel: LapanganViewModel
    private var selectedLapanganId: Int? = null
    private var selectedLapanganNama: String? = null
    private var selectedHarga: Int? = null
    private var latestBookingId: Int = 0
    private var latestTotalHarga: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lapanganViewModel = ViewModelProvider(this)[LapanganViewModel::class.java]
        lapanganViewModel.fetchLapangan()
        setupLapanganDropdown()

        binding.btnBooking.setOnClickListener {
            handleBookingSubmit()
        }

        // 🔁 Aksi untuk tombol Bayar Sekarang
        binding.btnBayarSekarang.setOnClickListener {
            if (latestTotalHarga != 0) {
                val intent = Intent(this, ManualPaymentActivity::class.java)
                intent.putExtra("booking_id", latestBookingId) // masih 0 karena belum ada dari backend
                intent.putExtra("total_harga", latestTotalHarga)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Lakukan booking terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupLapanganDropdown() {
        lapanganViewModel.lapanganList.observe(this) { lapanganList ->
            val namaList = lapanganList.map { it.nama }
            val lapanganMap = lapanganList.associateBy { it.nama }

            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, namaList)
            binding.etPilihLapangan.setAdapter(adapter)

            binding.etPilihLapangan.setOnItemClickListener { parent, _, position, _ ->
                val namaDipilih = parent.getItemAtPosition(position).toString()
                val lapangan = lapanganMap[namaDipilih]
                selectedLapanganId = lapangan?.id
                selectedLapanganNama = lapangan?.nama
                selectedHarga = lapangan?.harga_per_jam
                binding.tvStatusLapangan.text = "Harga: Rp ${lapangan?.harga_per_jam}"
            }
        }
    }

    private fun handleBookingSubmit() {
        val userId = 1 // ganti sesuai kebutuhan
        val lapanganId = selectedLapanganId
        val tanggal = binding.etTanggal.text.toString().trim()
        val jamMulai = binding.etJamMulai.text.toString().trim()
        val jamSelesai = binding.etJamSelesai.text.toString().trim()
        val metode = "cash"
        val status = "full"
        val repeat = false

        if (lapanganId == null || tanggal.isBlank() || jamMulai.isBlank() || jamSelesai.isBlank()) {
            Toast.makeText(this, "Lengkapi semua data booking", Toast.LENGTH_SHORT).show()
            return
        }

        val bookingRequest = BookingRequest(
            user_id = userId,
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
                    Toast.makeText(this@BookingActivity, "Booking berhasil!", Toast.LENGTH_SHORT).show()

                    // Hitung total harga booking
                    val jamMulaiInt = jamMulai.split(":")[0].toInt()
                    val jamSelesaiInt = jamSelesai.split(":")[0].toInt()
                    val durasiJam = jamSelesaiInt - jamMulaiInt
                    val hargaPerJam = selectedHarga ?: 0
                    val totalHarga = durasiJam * hargaPerJam

                    latestBookingId = 0 // backend belum kirim ID booking
                    latestTotalHarga = totalHarga

                    binding.btnBayarSekarang.visibility = View.VISIBLE

                    val local = Booking(
                        id = 0,
                        user_id = userId,
                        lapangan_id = lapanganId,
                        tanggal = tanggal,
                        jam_mulai = jamMulai,
                        jam_selesai = jamSelesai,
                        status = status,
                        metode_pembayaran = metode,
                        repeat_weekly = repeat
                    )
                    BookingStorage.saveBooking(this@BookingActivity, local)

                    // ❌ Jangan clear form agar user bisa langsung tekan "Bayar Sekarang"
                    // clearForm()
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("BOOKING_ERROR", "Error: $error")
                    Toast.makeText(this@BookingActivity, "Gagal booking: $error", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("BOOKING_EXCEPTION", "Exception: ${e.message}")
                Toast.makeText(this@BookingActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearForm() {
        binding.etTanggal.text.clear()
        binding.etJamMulai.text.clear()
        binding.etJamSelesai.text.clear()
        binding.etPilihLapangan.text.clear()
        binding.tvStatusLapangan.text = ""
        selectedLapanganId = null
        selectedLapanganNama = null
        selectedHarga = null
    }
}
