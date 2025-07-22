package com.example.bookinglapangan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.bookinglapangan.data.session.SessionManager
import com.example.bookinglapangan.databinding.ActivityMainBinding
import com.example.bookinglapangan.ui.booking.BookingActivity
import com.example.bookinglapangan.ui.lapangan.LapanganActivity
import com.example.bookinglapangan.ui.login.LoginActivity

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil nama user dari SharedPreferences
        val session = SessionManager(this)
        val name = session.getName() ?: "Pengguna"

        // Tampilkan nama
        binding.tvWelcome.text = "Selamat datang,\n$name"

        // Tombol ke BookingActivity
        binding.btnBooking.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }

        // Tombol ke LapanganActivity
        binding.btnCekLapangan.setOnClickListener {
            startActivity(Intent(this, LapanganActivity::class.java))
        }

        // Tombol Logout
        binding.btnLogout.setOnClickListener {
            session.clearSession()
            Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
