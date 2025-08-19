package com.example.bookinglapangan.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinglapangan.R

class BannerAdapter(
    private val images: List<Int> // pakai @Drawable, atau ubah ke url
) : RecyclerView.Adapter<BannerAdapter.VH>() {
    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val img: ImageView = v.findViewById(R.id.imgBanner)
    }
    override fun onCreateViewHolder(p: ViewGroup, vt: Int): VH =
        VH(LayoutInflater.from(p.context).inflate(R.layout.item_banner, p, false))
    override fun onBindViewHolder(h: VH, pos: Int) { h.img.setImageResource(images[pos]) }
    override fun getItemCount() = images.size
}
