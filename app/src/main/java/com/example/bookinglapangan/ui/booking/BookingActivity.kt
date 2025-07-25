package com.example.bookinglapangan.ui.booking

import android.os.Bundle
<<<<<<< HEAD
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bookinglapangan.data.api.RetrofitClient
import com.example.bookinglapangan.databinding.ActivityBookingBinding
import kotlinx.coroutines.launch

class BookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingBinding
=======
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bookinglapangan.databinding.ActivityBookingBinding
import com.example.bookinglapangan.data.pref.Lapangan
import com.example.bookinglapangan.ui.booking.BookingViewModel
import com.example.bookinglapangan.data.pref.LapanganViewModel

class BookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingBinding
    private lateinit var bookingViewModel: BookingViewModel
    private lateinit var lapanganViewModel: LapanganViewModel
>>>>>>> 0f717ec40a751538100d5e67f9067b42546afb5e

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

<<<<<<< HEAD
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
=======
        bookingViewModel = ViewModelProvider(this)[BookingViewModel::class.java]
        lapanganViewModel = ViewModelProvider(this)[LapanganViewModel::class.java]

        // Ambil data lapangan dari backend Laravel
        lapanganViewModel.fetchLapangan()

        // Tampilkan pilihan lapangan dan status
        lapanganViewModel.lapanganList.observe(this) { lapanganList ->
            val lapanganMap = lapanganList.associateBy { it.nama }
            val namaLapanganList = lapanganList.map { it.nama }

            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, namaLapanganList)
            binding.etPilihLapangan.setAdapter(adapter)

            binding.etPilihLapangan.setOnItemClickListener { parent, _, position, _ ->
                val namaDipilih = parent.getItemAtPosition(position).toString()
                val status = lapanganMap[namaDipilih]?.status ?: "-"
                binding.tvStatusLapangan.text = "Status: $status"
            }
        }

        // Simpan Booking
        binding.btnSimpan.setOnClickListener {
            val nama = binding.etNama.text.toString()
            val waktu = binding.etWaktu.text.toString()
            val lapanganDipilih = binding.etPilihLapangan.text.toString()

            if (nama.isNotBlank() && waktu.isNotBlank() && lapanganDipilih.isNotBlank()) {
                bookingViewModel.insertBooking(nama, waktu, lapanganDipilih)
                Toast.makeText(this, "Booking disimpan", Toast.LENGTH_SHORT).show()
                clearForm()
            } else {
                Toast.makeText(this, "Harap isi semua kolom!", Toast.LENGTH_SHORT).show()
            }
        }

        // Daftar booking lokal
        bookingViewModel.allBookings.observe(this) { list ->
            binding.tvList.text = list.joinToString("\n") { "${it.nama} - ${it.lapangan}" }
        }
    }

    private fun clearForm() {
        binding.etNama.text.clear()
        binding.etWaktu.text.clear()
        binding.etPilihLapangan.text.clear()
        binding.tvStatusLapangan.text = ""
>>>>>>> 0f717ec40a751538100d5e67f9067b42546afb5e
    }
}
