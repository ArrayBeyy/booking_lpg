package com.example.bookinglapangan.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinglapangan.R
import com.example.bookinglapangan.data.fragmen_logic.SportCategory

class CategoryAdapter(
    private val items: List<SportCategory>,
    private val onClick: (SportCategory) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val img: ImageView = v.findViewById(R.id.img)
        val tv: TextView = v.findViewById(R.id.tvTitle)

        fun bind(item: SportCategory) {
            tv.text = item.title
            img.setImageResource(item.imageRes)
            itemView.setOnClickListener { onClick(item) } // aman
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
