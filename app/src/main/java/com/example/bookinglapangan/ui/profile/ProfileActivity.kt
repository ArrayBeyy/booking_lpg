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

        val name = session.getName() ?: "-"
        val email = session.getEmail() ?: "-"
        val cabang = session.getCabang() ?: "-"
        val userId = session.getUserId()

        // Tampilkan ke TextView Profile
        binding.tvNama.text = name
        binding.tvEmail.text = email
        binding.tvCabang.text = cabang
        binding.tvUserId.text = "ID: $userId"
    }
}
