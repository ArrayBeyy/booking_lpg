package com.example.bookinglapangan.ui.lapangan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookinglapangan.data.model.LapanganItem
import com.example.bookinglapangan.databinding.ItemLapanganBinding

class LapanganAdapter(
    private val onClick: (LapanganItem) -> Unit
) : ListAdapter<LapanganItem, LapanganAdapter.VH>(diff) {

    object diff : DiffUtil.ItemCallback<LapanganItem>() {
        override fun areItemsTheSame(o: LapanganItem, n: LapanganItem) = o.id == n.id
        override fun areContentsTheSame(o: LapanganItem, n: LapanganItem) = o == n
    }

    inner class VH(val binding: ItemLapanganBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LapanganItem) = with(binding) {
            tvNamaLapangan.text   = item.nama
            tvHargaLapangan.text  = "${item.harga_per_jam / 1000}K /lapangan/jam"
            tvRating.text = (item.rating ?: 0f).toString()
            tvLokasiLapangan.text   = item.kota ?: "-"
            val url = item.foto.firstOrNull()
            Glide.with(root).load(url).into(ivFoto)

            // ⬇️ pakai 'item', bukan 'it' (View)
            root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLapanganBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}
