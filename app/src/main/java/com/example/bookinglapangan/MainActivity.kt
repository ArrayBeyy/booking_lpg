package com.example.bookinglapangan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import com.example.bookinglapangan.databinding.ActivityMainBinding
import com.example.bookinglapangan.ui.QR.ScanQrActivity
import com.example.bookinglapangan.ui.booking.HistoryBookingActivity
import com.example.bookinglapangan.ui.login.LoginActivity
import com.example.bookinglapangan.ui.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    // ✅ Tambah helper kecil biar rapi
    private fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.nav_home)
                    true
                }
                R.id.nav_news -> {
                    navController.navigate(R.id.nav_news)
                    true
                }
                R.id.nav_history -> {
                    if (isLoggedIn()) {
                        startActivity(Intent(this, HistoryBookingActivity::class.java))
                    } else {
                        // 🔁 Perubahan MINOR: kirim redirectTo = "history"
                        startActivity(
                            Intent(this, LoginActivity::class.java)
                                .putExtra("redirectTo", "history")
                        )
                    }
                    // tetap jangan pindah highlight tab
                    false
                }
                R.id.nav_akun -> {
                    if (isLoggedIn()) {
                        startActivity(Intent(this, ProfileActivity::class.java))
                    } else {
                        // 🔁 Perubahan MINOR: kirim redirectTo = "profile"
                        startActivity(
                            Intent(this, LoginActivity::class.java)
                                .putExtra("redirectTo", "profile")
                        )
                    }
                    // tetap di tab sekarang
                    false
                }
                else -> false
            }
        }

        binding.btnScanQr.setOnClickListener {
            startActivity(Intent(this, ScanQrActivity::class.java))
        }
    }
}
