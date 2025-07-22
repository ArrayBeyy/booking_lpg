package com.example.bookinglapangan.data.pref

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinglapangan.R

class LapanganAdapter(private val list: List<Lapangan>) : RecyclerView.Adapter<LapanganAdapter.LapanganViewHolder>() {

    class LapanganViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nama: TextView = view.findViewById(R.id.tvNamaLapangan)
        val status: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapanganViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lapangan, parent, false)
        return LapanganViewHolder(view)
    }

    override fun onBindViewHolder(holder: LapanganViewHolder, position: Int) {
        val lapangan = list[position]
        holder.nama.text = lapangan.nama
        holder.status.text = lapangan.status.capitalize()

        if (lapangan.status == "tersedia") {
            holder.status.setTextColor(Color.GREEN)
        } else {
            holder.status.setTextColor(Color.RED)
        }
    }

    override fun getItemCount() = list.size
}
