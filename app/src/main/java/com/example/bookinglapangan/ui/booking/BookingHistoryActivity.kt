package com.example.bookinglapangan.ui.booking

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookinglapangan.databinding.ActivityBookingHistoryBinding
import com.example.bookinglapangan.data.session.SessionManager
import com.example.bookinglapangan.model.toDisplay
import com.example.bookinglapangan.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class HistoryBookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingHistoryBinding
    private lateinit var adapter: HistoryBookingAdapter
    private val historyList = mutableListOf<BookingDisplay>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val session = SessionManager(this)

        if (currentUser == null && session.getName() == null) {
            // User BELUM login → tampilkan guest layout
            showGuestLayout()

            binding.btnLoginGuest.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("redirectTo", "history")  // ⬅️ Kirim informasi tujuan
                startActivity(intent)}

        } else {
            // User SUDAH login → tampilkan konten user
            showUserLayout()
            setupRecyclerView()
            loadHistory()
        }

        binding.btnLoginGuest.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        adapter = HistoryBookingAdapter(historyList)
        binding.rvHistoryBooking.layoutManager = LinearLayoutManager(this)
        binding.rvHistoryBooking.adapter = adapter
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

    private fun showGuestLayout() {
        binding.guestLayout.visibility = android.view.View.VISIBLE
        binding.rvHistoryBooking.visibility = android.view.View.GONE
    }

    private fun showUserLayout() {
        binding.guestLayout.visibility = android.view.View.GONE
        binding.rvHistoryBooking.visibility = android.view.View.VISIBLE
    }
}
