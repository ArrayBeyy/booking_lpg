package com.example.bookinglapangan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.bookinglapangan.databinding.ActivityMainBinding
import com.example.bookinglapangan.ui.booking.BookingActivity
import com.example.bookinglapangan.ui.lapangan.LapanganActivity
import com.example.bookinglapangan.ui.login.LoginActivity
import com.example.bookinglapangan.ui.login.LoginViewModel

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tampilkan nama/email user
        val email = viewModel.getLoggedInEmail()
        binding.tvWelcome.text = "Selamat datang,\n$email"

        // Tombol ke BookingActivity
        binding.btnBooking.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }

        // Tombol ke LapanganActivity (cek ketersediaan)
        binding.btnCekLapangan.setOnClickListener {
            startActivity(Intent(this, LapanganActivity::class.java))
        }

        // Tombol Logout
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
