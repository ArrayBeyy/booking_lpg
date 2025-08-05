package com.example.bookinglapangan.ui.booking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinglapangan.R
import com.example.bookinglapangan.data.session.SessionManager
import com.example.bookinglapangan.ui.booking.Booking
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
        val session = SessionManager(this)
        val currentUserId = session.getUserId()  // Ambil user_id dari Session

        // Dummy data booking (simulasi dari database)
        val dummyBookings = listOf(
            Booking(
                id = 101,
                user_id = 1,
                lapangan_id = 1,
                tanggal = "2025-08-01",
                jam_mulai = "10:00",
                jam_selesai = "11:00",
                status = "selesai",
                lapangan_nama = "Lapangan A"
            ),
            Booking(
                id = 102,
                user_id = 2,  // Booking user lain
                lapangan_id = 2,
                tanggal = "2025-08-02",
                jam_mulai = "09:00",
                jam_selesai = "10:00",
                status = "selesai",
                lapangan_nama = "Lapangan B"
            )
        )

        // Filter hanya milik user yang sedang login
        val userBookings = dummyBookings.filter { it.user_id == currentUserId }

        historyList.clear()
        historyList.addAll(userBookings.map { it.toDisplay() })

        adapter.notifyDataSetChanged()
    }
}
