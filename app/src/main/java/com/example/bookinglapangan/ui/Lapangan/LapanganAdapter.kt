package com.example.bookinglapangan.ui.lapangan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinglapangan.R
import com.example.bookinglapangan.ui.Lapangan.Lapangan

class LapanganAdapter(
    private val items: List<Lapangan>,
    private val onClick: (Lapangan) -> Unit
) : RecyclerView.Adapter<LapanganAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        private val card: CardView = v as CardView
        private val tvNama: TextView = v.findViewById(R.id.tvNamaLapangan)
        private val tvLokasi: TextView = v.findViewById(R.id.tvLokasiLapangan)
        private val tvHarga: TextView = v.findViewById(R.id.tvHargaLapangan)

        fun bind(item: Lapangan) {
            tvNama.text = item.nama
            tvLokasi.text = "Lokasi: ${item.lokasi}"
            tvHarga.text = "Rp ${item.harga_per_jam}"
            card.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lapangan, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size
}
