package com.example.bookinglapangan.ui.login

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinglapangan.MainActivity
import com.example.bookinglapangan.R
import com.example.bookinglapangan.databinding.ActivityLoginBinding
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Simpan device_id jika belum ada
        val devicePref = getSharedPreferences("DEVICE_PREF", Context.MODE_PRIVATE)
        if (devicePref.getString("DEVICE_ID", null) == null) {
            val deviceId = "android-${UUID.randomUUID()}"
            devicePref.edit().putString("DEVICE_ID", deviceId).apply()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isBlank()) {
                binding.etEmail.error = "Email wajib diisi"
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmail.error = "Format email tidak valid"
                return@setOnClickListener
            }
            if (password.isBlank()) {
                binding.etPassword.error = "Password wajib diisi"
                return@setOnClickListener
            }

            val shared = getSharedPreferences("USER_LOCAL", Context.MODE_PRIVATE)
            val savedEmail = shared.getString("EMAIL", null)
            val savedPassword = shared.getString("PASSWORD", null)

            if (email == savedEmail && password == savedPassword) {
                Toast.makeText(this, "Login lokal berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegister.setOnClickListener {
            showRegisterDialog()
        }
    }

    private fun showRegisterDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.activity_register, null)
        val etName = view.findViewById<EditText>(R.id.etName)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)

        AlertDialog.Builder(this)
            .setTitle("Daftar Pengguna")
            .setView(view)
            .setPositiveButton("Daftar") { _, _ ->
                val name = etName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (name.isBlank() || email.isBlank() || password.isBlank()) {
                    Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (password.length < 6) {
                    Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val shared = getSharedPreferences("USER_LOCAL", Context.MODE_PRIVATE)
                shared.edit()
                    .putString("NAME", name)
                    .putString("EMAIL", email)
                    .putString("PASSWORD", password)
                    .apply()

                Toast.makeText(this, "Registrasi berhasil. Silakan login.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
