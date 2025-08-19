package com.example.bookinglapangan.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinglapangan.MainActivity
import com.example.bookinglapangan.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Pakai tema splash biar tampil seketika
        setTheme(R.style.Theme_BookingLapangan_Splash)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Tahan 5 detik lalu masuk ke MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish() // tutup splash agar tidak kembali ke sini saat back
        }, 5000)
    }
}
