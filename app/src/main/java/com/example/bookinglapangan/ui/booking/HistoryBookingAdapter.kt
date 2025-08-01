package com.example.bookinglapangan.ui.booking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinglapangan.R
import com.example.bookinglapangan.ui.booking.BookingDisplay

class HistoryBookingAdapter(private val bookings: List<BookingDisplay>) :
    RecyclerView.Adapter<HistoryBookingAdapter.BookingViewHolder>() {

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lapangan: TextView = itemView.findViewById(R.id.tvLapangan)
        val tanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val waktu: TextView = itemView.findViewById(R.id.tvWaktu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking_history, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.lapangan.text = booking.lapangan
        holder.tanggal.text = booking.tanggal
        holder.waktu.text = booking.waktu
    }

    override fun getItemCount(): Int = bookings.size
}
