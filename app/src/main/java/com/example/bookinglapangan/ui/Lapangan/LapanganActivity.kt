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
        btnBookingNow?.isEnabled = false
        btnBookingNow?.setOnClickListener {
            selectedLapangan?.let { lap ->
                val go = Intent(this, com.example.bookinglapangan.ui.booking.BookingActivity::class.java)
                go.putExtra("lapangan_nama", lap.nama)
                go.putExtra("lapangan_lokasi", lap.lokasi)
                go.putExtra("harga_per_jam", lap.harga_per_jam)
                startActivity(go)
            }
        }

        // --- Aktifkan tombol bila datang dari layar sebelumnya (data dikirim via Intent) ---
        val namaFromIntent = intent.getStringExtra("lapangan_nama")
        if (namaFromIntent != null) {
            val lokasiFromIntent = intent.getStringExtra("lapangan_lokasi") ?: ""
            val hargaFromIntent  = intent.getIntExtra("harga_per_jam", 0)   // jika Double di model: getDoubleExtra(..., 0.0)
            val idFromIntent     = intent.getIntExtra("lapangan_id", -1)

            // seed ke selectedLapangan supaya onClick yang sudah ada langsung jalan
            selectedLapangan = Lapangan(
                id = idFromIntent,
                nama = namaFromIntent,
                lokasi = lokasiFromIntent,
                harga_per_jam = hargaFromIntent
            )

            btnBookingNow?.isEnabled = true
            btnBookingNow?.alpha = 1f
            btnBookingNow?.bringToFront()
            btnBookingNow?.isClickable = true
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
                        selectedLapangan = lapangan
                        btnBookingNow?.isEnabled = true
                    }
                }

                override fun getItemCount(): Int = list.size
            }
        }

        viewModel.fetchLapangan()
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
