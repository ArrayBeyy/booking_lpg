package com.example.bookinglapangan.ui.booking

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinglapangan.R
import com.example.bookinglapangan.data.api.RetrofitClient
import kotlinx.coroutines.launch

class BookingHistoryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_history)

        recyclerView = findViewById(R.id.rvBookingHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getBookingHistory()
                if (response.isSuccessful && response.body() != null) {
                    val bookings = response.body()!!.data
                    adapter = BookingAdapter(bookings)
                    recyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                Toast.makeText(this@BookingHistoryActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
