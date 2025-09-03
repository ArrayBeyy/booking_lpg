package com.example.bookinglapangan.ui.lapangan

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.bookinglapangan.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class LapanganDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        com.google.android.material.color.DynamicColors.applyIfAvailable(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lapangan_detail)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Ambil data minimal dari intent
        val id    = intent.getIntExtra("lapangan_id", -1)
        val nama  = intent.getStringExtra("lapangan_nama") ?: "-"
        val lokasi= intent.getStringExtra("lapangan_lokasi") ?: "-"
        val harga = intent.getIntExtra("harga_per_jam", 0)
        val cover = intent.getStringExtra("cover_image")
        val rating= intent.getDoubleExtra("rating", 5.0)

        // Bind UI
        findViewById<TextView>(R.id.tvNama).text = nama
        findViewById<TextView>(R.id.tvLokasi).text = lokasi
        findViewById<TextView>(R.id.tvHarga).text = "Rp %,d/jam".format(harga).replace(",", ".")
        findViewById<RatingBar>(R.id.ratingBar).rating = rating.toFloat()

        val iv = findViewById<ImageView>(R.id.ivCover)
        if (cover.isNullOrBlank()) {
            iv.setImageResource(R.drawable.banner_badminton)
        } else {
            iv.load(cover) { crossfade(true) }
        }

        // TODO: Kalau mau tarik detail lengkap dari API /api/lapangans/{id}, tambahkan call di sini
        // dan isi: jumlah_lapangan, jam_buka/tutup, fasilitas[], deskripsi

        findViewById<MaterialButton>(R.id.btnPilihJadwal).setOnClickListener {
            val go = Intent(this, com.example.bookinglapangan.ui.booking.BookingActivity::class.java)
            go.putExtra("lapangan_id", id)
            go.putExtra("lapangan_nama", nama)
            go.putExtra("lapangan_lokasi", lokasi)
            go.putExtra("harga_per_jam", harga)
            startActivity(go)
        }
    }
}
