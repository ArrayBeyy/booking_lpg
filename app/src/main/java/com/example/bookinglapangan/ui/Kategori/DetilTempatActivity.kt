package com.example.bookinglapangan.ui.Kategori

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.bookinglapangan.data.api.ApiService
import com.example.bookinglapangan.data.model.LapanganDetailDto
import com.example.bookinglapangan.data.api.RetrofitClient
import com.example.bookinglapangan.databinding.ActivityDetilTempatBinding
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class DetilTempatActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LAPANGAN_ID = "lapangan_id"
    }

    private lateinit var binding: ActivityDetilTempatBinding
    private val api: ApiService by lazy { RetrofitClient.apiService }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetilTempatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // edge-to-edge insets (opsional)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, b.bottom); insets
        }

        binding.topAppBar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val id = intent.getIntExtra(EXTRA_LAPANGAN_ID, 0)
        if (id == 0) {
            Toast.makeText(this, "Lapangan ID tidak valid", Toast.LENGTH_SHORT).show()
            finish(); return
        }

        fetchDetail(id)
    }

    private fun fetchDetail(id: Int) {
        setLoading(true)
        lifecycleScope.launch {
            try {
                val dto = api.getLapanganDetail(id)
                bindUi(dto)
            } catch (e: Exception) {
                Toast.makeText(this@DetilTempatActivity, "Gagal memuat: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun bindUi(d: LapanganDetailDto) = with(binding) {
        // Cover
        if (!d.coverImage.isNullOrBlank()) {
            Glide.with(ivCover).load(d.coverImage).into(ivCover)
        } else ivCover.setImageDrawable(null)

        // Nama, Kategori, Lokasi
        tvNama.setText(d.nama)
        tvKategori.setText(d.kategori)
        tvLokasi.setText(d.lokasi)

        // Harga / jam (IDR)
        val rupiah = NumberFormat.getCurrencyInstance(Locale("in","ID"))
        tvHarga.setText(rupiah.format(d.hargaPerJam).replace(",00",""))

        // Jumlah lapangan
        tvJumlah.setText(d.jumlahLapangan.toString())

        // Jam buka / tutup → tampil 06:00 AM / 11:00 PM seperti di form admin
        tvBuka.setText(toAmPm(d.buka))
        tvTutup.setText(toAmPm(d.tutup))

        // Fasilitas → Chip
        chipGroupFasilitas.removeAllViews()
        d.fasilitas.forEach { f ->
            val chip = Chip(this@DetilTempatActivity).apply {
                text = f; isCheckable = false; isClickable = false
            }
            chipGroupFasilitas.addView(chip)
        }

        // Rating
        ratingBar.rating = d.rating
        tvRatingValue.text = String.format(Locale.US, "%.1f", d.rating)

        // Deskripsi
        etDeskripsi.setText(d.deskripsi ?: "-")

        // CTA (kalau perlu)
        btnPilihJadwal.setOnClickListener {
            Toast.makeText(this@DetilTempatActivity, "Pilih jadwal untuk ${d.nama}", Toast.LENGTH_SHORT).show()
            // TODO: arahkan ke PilihJadwalActivity, kirim id & harga
        }
    }

    private fun toAmPm(src: String): String {
        // menerima "06:00" atau "06:00:00" → kembalikan "06:00 AM"
        val parts = src.trim().split(":")
        if (parts.size < 2) return src
        var h = parts[0].toIntOrNull() ?: return src
        val m = parts[1]
        val am = if (h < 12) "AM" else "PM"
        if (h == 0) h = 12
        if (h > 12) h -= 12
        return String.format(Locale.US, "%02d:%s %s", h, m, am)
    }

    private fun setLoading(show: Boolean) = with(binding) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        contentContainer.visibility = if (show) View.GONE else View.VISIBLE
    }
}
