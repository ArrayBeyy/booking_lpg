package com.example.bookinglapangan.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinglapangan.R
import com.example.bookinglapangan.databinding.ActivityProfileBinding
import com.example.bookinglapangan.data.session.SessionManager
import com.example.bookinglapangan.ui.login.LoginActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val session = SessionManager(this)

        val name = session.getName() ?: "-"
        val email = session.getEmail() ?: "-"
        val cabang = session.getCabang() ?: "-"
        val userId = session.getUserId()
        val btnLogout = findViewById<Button>(R.id.btnLogout)


        btnLogout.setOnClickListener {
            val session = SessionManager(this)
            session.setLoginStatus(false)  // logout
            session.clearSession()         // hapus data sesi (opsional)

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Tampilkan ke TextView Profile
        binding.tvNama.text = name
        binding.tvEmail.text = email
        binding.tvCabang.text = cabang
        binding.tvUserId.text = "ID: $userId"
    }
}
