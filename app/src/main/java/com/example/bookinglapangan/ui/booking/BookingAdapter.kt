package com.example.bookinglapangan.ui.booking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinglapangan.R

class BookingAdapter(private val list: List<Booking>) :
    RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    class BookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvLapangan: TextView = view.findViewById(R.id.tvLapangan)
        val tvTanggal: TextView = view.findViewById(R.id.tvTanggal)
        val tvJam: TextView = view.findViewById(R.id.tvJam)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val item = list[position]
        holder.tvLapangan.text = "Lapangan ID: ${item.lapangan_id}"
        holder.tvTanggal.text = "Tanggal: ${item.tanggal}"
        holder.tvJam.text = "Jam: ${item.jam_mulai} - ${item.jam_selesai}"
        holder.tvStatus.text = "Status: ${item.status}"
    }

    override fun getItemCount(): Int = list.size
}
