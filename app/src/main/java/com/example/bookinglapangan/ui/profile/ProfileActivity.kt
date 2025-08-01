package com.example.bookinglapangan.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinglapangan.databinding.ActivityProfileBinding
import com.example.bookinglapangan.data.session.SessionManager

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val session = SessionManager(this)

        // Ambil data user yang disimpan saat register/login
        val name = session.getName() ?: "Nama tidak tersedia"
        val email = session.getEmail() ?: "Email tidak tersedia"
        val cabang = session.getCabang() ?: "Cabang tidak tersedia"

        // Tampilkan ke TextView
        binding.tvNama.text = name
        binding.tvEmail.text = email
        binding.tvCabang.text = cabang
    }
}
