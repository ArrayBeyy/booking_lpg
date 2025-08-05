package com.example.bookinglapangan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bookinglapangan.databinding.ActivityMainBinding
import com.example.bookinglapangan.data.session.SessionManager
import com.example.bookinglapangan.ui.QR.ScanQrActivity
import com.example.bookinglapangan.ui.booking.BookingActivity
import com.example.bookinglapangan.ui.booking.HistoryBookingActivity
import com.example.bookinglapangan.ui.lapangan.LapanganActivity
import com.example.bookinglapangan.ui.login.LoginActivity
import com.example.bookinglapangan.ui.profile.ProfileActivity
import com.example.bookinglapangan.ui.store.PenjualanActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Cek apakah user sudah login
        /*if (firebaseAuth.currentUser == null) {
            // Jika belum login, redirect ke LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }*/

        // Ambil nama user dari SharedPreferences via SessionManager
        val session = SessionManager(this)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val name = if (currentUser != null) {
            currentUser.displayName ?: "Pengguna"
        } else {
            SessionManager(this).getName() ?: "Pengguna"
        }
        binding.tvWelcome.text = "Selamat datang,\n$name"

        //BookingActivity
        binding.btnBooking.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }

        // BottomNavigation Listener
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    true
                }
                R.id.nav_pesanan -> {
                    if (firebaseAuth.currentUser != null) {
                        startActivity(Intent(this, HistoryBookingActivity::class.java))
                    } else {
                        Toast.makeText(this, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    true
                }
                R.id.nav_grup -> {
                    startActivity(Intent(this, LapanganActivity::class.java))
                    true
                }
                R.id.nav_akun -> {
                    if (firebaseAuth.currentUser != null) {
                        startActivity(Intent(this, ProfileActivity::class.java))
                    } else {
                        Toast.makeText(this, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    true
                }
                else -> false
            }
        }

        //store
        binding.btnPenjualan.setOnClickListener {
            val intent = Intent(this, PenjualanActivity::class.java)
            startActivity(intent)
        }

        // FloatingActionButton (QR Scan)
        binding.btnScanQr.setOnClickListener {
            val intent = Intent(this, ScanQrActivity::class.java)
            startActivity(intent)
        }

        //Logout
        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut() // <-- Logout dari Firebase
            session.clearSession()
            Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}

