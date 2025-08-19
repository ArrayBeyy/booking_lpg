package com.example.bookinglapangan.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinglapangan.R
import com.example.bookinglapangan.databinding.ActivityProfileBinding
import com.example.bookinglapangan.data.session.SessionManager
import com.example.bookinglapangan.MainActivity
import com.example.bookinglapangan.ui.login.LoginActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val session = SessionManager(this)

        // ✅ 1) Jika BELUM login, arahkan ke Login dengan redirectTo="profile"
        if (!session.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java).apply {
                putExtra("redirectTo", "profile") // penting!
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            finish()
            return
        }

        val name = session.getName() ?: "-"
        val email = session.getEmail() ?: "-"
        val cabang = session.getCabang() ?: "-"
        val userId = session.getUserId()
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnLogout.setOnClickListener {
            val s = SessionManager(this)
            s.setLoginStatus(false)   // logout
            s.clearSession()          // hapus data sesi (opsional)

            // ✅ 2) Setelah logout, bersihkan back stack dan kembali ke Main (atau ke Login kalau kamu mau)
            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            finish()
        }

        // Tampilkan ke TextView Profile
        binding.tvNama.text = name
        binding.tvEmail.text = email
        binding.tvCabang.text = cabang
        binding.tvUserId.text = "ID: $userId"
    }
}
