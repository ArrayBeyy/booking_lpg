package com.example.bookinglapangan.ui.booking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinglapangan.R
import com.example.bookinglapangan.ui.booking.BookingDisplay
import com.example.bookinglapangan.model.toDisplay

class HistoryBookingActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryBookingAdapter
    private val historyList = mutableListOf<BookingDisplay>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_history)

        recyclerView = findViewById(R.id.rvHistoryBooking)
        adapter = HistoryBookingAdapter(historyList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadHistory()
    }

    private fun loadHistory() {
        // Contoh dummy Booking (simulasi dari database)
        val dummyBooking = Booking(
            id = 101,
            user_id = 1,
            lapangan_id = 1,
            tanggal = "2025-08-01",
            jam_mulai = "10:00",
            jam_selesai = "11:00",
            status = "selesai",
            lapangan_nama = "Lapangan A"
        )

        val dummyBooking2 = dummyBooking.copy(
            id = 102,
            tanggal = "2025-08-03",
            jam_mulai = "08:00",
            jam_selesai = "09:00",
            lapangan_nama = "Lapangan B"
        )

        historyList.add(dummyBooking.toDisplay())
        historyList.add(dummyBooking2.toDisplay())

        adapter.notifyDataSetChanged()
    }
}
