package com.example.bookinglapangan.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinglapangan.MainActivity
import com.example.bookinglapangan.R
import com.example.bookinglapangan.data.session.SessionManager
import com.example.bookinglapangan.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnToRegister = findViewById<Button>(R.id.btnToRegister)

        btnToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val emailInput = etEmail.text.toString()
            val passwordInput = etPassword.text.toString()

            val session = SessionManager(this)
            val savedEmail = session.getEmail()
            val savedPassword = session.getPassword()

            if (emailInput == savedEmail && passwordInput == savedPassword) {
                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
            }
        }
    }
}