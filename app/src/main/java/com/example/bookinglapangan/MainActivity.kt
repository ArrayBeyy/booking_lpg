package com.example.bookinglapangan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
<<<<<<< HEAD
import com.example.bookinglapangan.data.session.SessionManager
=======
import androidx.activity.viewModels
>>>>>>> 0f717ec40a751538100d5e67f9067b42546afb5e
import com.example.bookinglapangan.databinding.ActivityMainBinding
import com.example.bookinglapangan.ui.booking.BookingActivity
import com.example.bookinglapangan.ui.lapangan.LapanganActivity
import com.example.bookinglapangan.ui.login.LoginActivity
<<<<<<< HEAD

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
=======
import com.example.bookinglapangan.ui.login.LoginViewModel

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: LoginViewModel by viewModels()
>>>>>>> 0f717ec40a751538100d5e67f9067b42546afb5e

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

<<<<<<< HEAD
        // Ambil nama user dari SharedPreferences
        val session = SessionManager(this)
        val name = session.getName() ?: "Pengguna"

        // Tampilkan nama
        binding.tvWelcome.text = "Selamat datang,\n$name"
=======
        // Tampilkan nama/email user
        val email = viewModel.getLoggedInEmail()
        binding.tvWelcome.text = "Selamat datang,\n$email"
>>>>>>> 0f717ec40a751538100d5e67f9067b42546afb5e

        // Tombol ke BookingActivity
        binding.btnBooking.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }

<<<<<<< HEAD
        // Tombol ke LapanganActivity
=======
        // Tombol ke LapanganActivity (cek ketersediaan)
>>>>>>> 0f717ec40a751538100d5e67f9067b42546afb5e
        binding.btnCekLapangan.setOnClickListener {
            startActivity(Intent(this, LapanganActivity::class.java))
        }

        // Tombol Logout
        binding.btnLogout.setOnClickListener {
<<<<<<< HEAD
            session.clearSession()
=======
            viewModel.logout()
>>>>>>> 0f717ec40a751538100d5e67f9067b42546afb5e
            Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
