package com.example.bookinglapangan.ui.lapangan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookinglapangan.R
import com.example.bookinglapangan.ui.Lapangan.Lapangan
import com.example.bookinglapangan.ui.Lapangan.LapanganViewModel
import com.example.bookinglapangan.ui.adapter.GridSpacingItemDecoration
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class LapanganActivity : AppCompatActivity() {
    private lateinit var viewModel: LapanganViewModel
    private lateinit var recyclerView: RecyclerView

    // simpan pilihan user
    private var selectedLapangan: Lapangan? = null
    private var btnBookingNow: MaterialButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        com.google.android.material.color.DynamicColors.applyIfAvailable(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lapangan)

        // ==== Toolbar (jika tersedia di layout) ====
        findViewById<MaterialToolbar?>(R.id.toolbar)?.let { tb ->
            setSupportActionBar(tb)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            tb.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            // set judul dari intent jika ada, fallback default
            supportActionBar?.title = intent.getStringExtra("lapangan_nama") ?: "Pilih Lapangan"
        }

        recyclerView = findViewById(R.id.rvLapangan)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // spacing + padding bawah agar tidak ketutup tombol
        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(GridSpacingItemDecoration(2, dp(12), true))
        }
        if (recyclerView.paddingBottom < dp(96)) {
            recyclerView.setPadding(
                recyclerView.paddingLeft,
                recyclerView.paddingTop,
                recyclerView.paddingRight,
                dp(96)
            )
            recyclerView.clipToPadding = false
        }

        btnBookingNow = findViewById(R.id.btnBookingNow)
        // 📌 Booking dari layar list tidak dipakai lagi — pindah ke halaman detail
        btnBookingNow?.visibility = View.GONE
        btnBookingNow?.isEnabled = false

        // --- Aktifkan tombol bila datang dari layar sebelumnya (data dikirim via Intent) ---
        val namaFromIntent = intent.getStringExtra("lapangan_nama")
        if (namaFromIntent != null) {
            val lokasiFromIntent = intent.getStringExtra("lapangan_lokasi") ?: ""
            val hargaFromIntent  = intent.getIntExtra("harga_per_jam", 0)   // jika Double di model: getDoubleExtra(..., 0.0)
            val idFromIntent     = intent.getIntExtra("lapangan_id", -1)

            // seed ke selectedLapangan (tidak mempengaruhi flow baru)
            selectedLapangan = Lapangan(
                id = idFromIntent,
                nama = namaFromIntent,
                lokasi = lokasiFromIntent,
                harga_per_jam = hargaFromIntent
            )
        }

        viewModel = ViewModelProvider(this)[LapanganViewModel::class.java]

        viewModel.lapanganList.observe(this) { list: List<Lapangan> ->
            recyclerView.adapter = object : RecyclerView.Adapter<LapanganViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapanganViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_lapangan, parent, false)
                    return LapanganViewHolder(view)
                }

                override fun onBindViewHolder(holder: LapanganViewHolder, position: Int) {
                    val lapangan = list[position]
                    holder.bind(lapangan)

                    holder.itemView.setOnClickListener {
                        // 👉 Klik item: buka halaman detail (Detil Tempat)
                        val go = Intent(this@LapanganActivity, LapanganDetailActivity::class.java)
                        go.putExtra("lapangan_id", lapangan.id)
                        go.putExtra("lapangan_nama", lapangan.nama)
                        go.putExtra("lapangan_lokasi", lapangan.lokasi)
                        go.putExtra("harga_per_jam", lapangan.harga_per_jam)
                        // jika model punya field opsional (cover_image, rating), ikutkan:
                        try {
                            val cover = lapangan.javaClass.getDeclaredField("cover_image").apply { isAccessible = true }.get(lapangan) as? String
                            val rating = (lapangan.javaClass.getDeclaredField("rating").apply { isAccessible = true }.get(lapangan) as? Number)?.toDouble()
                            go.putExtra("cover_image", cover)
                            go.putExtra("rating", rating ?: 5.0)
                        } catch (_: Throwable) { /* abaikan jika tidak ada */ }
                        startActivity(go)
                    }
                }

                override fun getItemCount(): Int = list.size
            }
        }

        val jenis = intent.getStringExtra("jenis_olahraga")    // atau null kalau mau semua
        val sportId = intent.getIntExtra("sport_id", -1).takeIf { it > 0 }
        viewModel.fetchLapangan(jenis = jenis, sportId = sportId)
    }

    class LapanganViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNama: TextView = itemView.findViewById(R.id.tvNamaLapangan)
        private val tvLokasi: TextView = itemView.findViewById(R.id.tvLokasiLapangan)
        private val tvHarga: TextView = itemView.findViewById(R.id.tvHargaLapangan)

        fun bind(lapangan: Lapangan) {
            tvNama.text = lapangan.nama
            tvLokasi.text = "Lokasi: ${lapangan.lokasi}"
            tvHarga.text = "Rp ${lapangan.harga_per_jam}"
        }
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
}
