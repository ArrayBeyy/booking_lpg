package com.example.bookinglapangan.ui.booking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bookinglapangan.data.api.RetrofitClient
import com.example.bookinglapangan.databinding.ActivityBookingBinding
import com.example.bookinglapangan.ui.Lapangan.LapanganViewModel
import com.example.bookinglapangan.ui.payment.XenditWebViewActivity
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private lateinit var lapanganViewModel: LapanganViewModel
    private var selectedLapanganId: Int? = null
    private var selectedLapanganNama: String? = null
    private var selectedHarga: Int? = null
    private var latestBookingId: Int = 0
    private var latestTotalHarga: Int = 0

    // CHANGE: Activity Result untuk menerima status dari XenditWebViewActivity
    private val payLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val status = result.data!!.getStringExtra("status") ?: "UNKNOWN"
            val externalId = result.data!!.getStringExtra("external_id").orEmpty()
            Toast.makeText(this, "Pembayaran: $status", Toast.LENGTH_LONG).show()
            Log.d("PAYMENT_RESULT", "external_id=$externalId, status=$status")
            // TODO: update UI/DB sesuai status. Misal: mark booking sebagai paid jika status == "PAID"
        } else {
            Toast.makeText(this, "Pembayaran dibatalkan / gagal", Toast.LENGTH_SHORT).show()
        }
    }

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

        // CHANGE: tombol Bayar Sekarang sekarang memanggil createInvoice ke backend,
        // lalu membuka XenditWebViewActivity dengan extras yang BENAR (pakai constants)
        binding.btnBayarSekarang.setOnClickListener {
            // Pastikan sudah ada total harga
            val amount = if (latestTotalHarga > 0) latestTotalHarga else hitungTotalHargaDariForm()
            val lapanganId = selectedLapanganId

            if (lapanganId == null || amount <= 0) {
                Toast.makeText(this, "Lakukan booking / lengkapi data dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startPayment(amount, lapanganId)
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
        val userId = 1 // TODO: ganti sesuai user login kamu
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
                    val totalHarga = hitungTotalHargaDariForm()
                    latestBookingId = 0 // TODO: isi dari response backend jika sudah mengembalikan id booking
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

                    // Biarkan form terisi agar user bisa langsung tekan Bayar Sekarang
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

    // CHANGE: fungsi hitung total harga (dipakai juga saat bayar sekarang kalau user belum tekan booking)
    private fun hitungTotalHargaDariForm(): Int {
        val jamMulai = binding.etJamMulai.text.toString().trim()
        val jamSelesai = binding.etJamSelesai.text.toString().trim()
        val hargaPerJam = selectedHarga ?: 0
        return try {
            val jamMulaiInt = jamMulai.split(":")[0].toInt()
            val jamSelesaiInt = jamSelesai.split(":")[0].toInt()
            val durasiJam = (jamSelesaiInt - jamMulaiInt).coerceAtLeast(0)
            durasiJam * hargaPerJam
        } catch (_: Exception) {
            0
        }
    }

    // CHANGE: panggil backend untuk create invoice → buka XenditWebViewActivity
    private fun startPayment(amount: Int, lapanganId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val body = mapOf(
                    "amount" to amount,          // Int
                    "lapangan_id" to lapanganId, // Int
                    "description" to "Booking Lapangan #$lapanganId"
                )
                val resp = RetrofitClient.apiService.createPayment(body)

                withContext(Dispatchers.Main) {
                    val invoiceUrl = resp.invoice_url ?: ""
                    val externalId = resp.external_id ?: ""

                    if (invoiceUrl.isBlank() || externalId.isBlank()) {
                        Toast.makeText(this@BookingActivity, "Invoice tidak valid", Toast.LENGTH_LONG).show()
                        return@withContext
                    }

                    val intent = Intent(this@BookingActivity, XenditWebViewActivity::class.java).apply {
                        putExtra(XenditWebViewActivity.EXTRA_INVOICE_URL, invoiceUrl)
                        putExtra(XenditWebViewActivity.EXTRA_EXTERNAL_ID, externalId)
                    }
                    startActivity(intent)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@BookingActivity, "Gagal create payment: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}