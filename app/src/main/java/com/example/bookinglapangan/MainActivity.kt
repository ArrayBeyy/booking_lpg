package com.example.bookinglapangan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.bookinglapangan.databinding.ActivityMainBinding
import com.example.bookinglapangan.ui.booking.BookingActivity
import com.example.bookinglapangan.ui.booking.HistoryBookingActivity
import com.example.bookinglapangan.ui.lapangan.LapanganActivity
import com.example.bookinglapangan.ui.login.LoginActivity
import com.example.bookinglapangan.data.session.SessionManager
import com.example.bookinglapangan.ui.profile.ProfileActivity
import com.example.bookinglapangan.ui.store.PenjualanActivity

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil nama user dari SharedPreferences via SessionManager
        val session = SessionManager(this)
        val name = session.getName() ?: "Pengguna"
        val btnPenjualan = findViewById<Button>(R.id.btnPenjualan)


        // Tampilkan teks sapaan
        binding.tvWelcome.text = "Selamat datang,\n$name"

        //BookingActivity
        binding.btnBooking.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }

        //LapanganActivity
        binding.btnCekLapangan.setOnClickListener {
            startActivity(Intent(this, LapanganActivity::class.java))
        }

        //HistoryBookingActivity
        binding.btnHistoryBooking.setOnClickListener {
            startActivity(Intent(this, HistoryBookingActivity::class.java))
        }

        //profile
        binding.btnProfil.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        //store
        btnPenjualan.setOnClickListener {
            val intent = Intent(this, PenjualanActivity::class.java)
            startActivity(intent)
        }


        //Logout
        binding.btnLogout.setOnClickListener {
            session.clearSession()
            Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
