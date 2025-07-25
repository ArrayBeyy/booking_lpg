package com.example.bookinglapangan.ui.login

<<<<<<< HEAD
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
=======
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinglapangan.MainActivity
import com.example.bookinglapangan.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("login_session", Context.MODE_PRIVATE)

        // ✅ Tombol Login
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            val registeredEmail = sharedPref.getString("registered_email", null)
            val registeredPassword = sharedPref.getString("registered_password", null)

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (email == registeredEmail && password == registeredPassword) {
                    sharedPref.edit().putString("email", email).apply()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Email atau password salah!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Isi email dan password!", Toast.LENGTH_SHORT).show()
            }
        }

        // ✅ Tombol ke halaman Register
        binding.btnToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
>>>>>>> 0f717ec40a751538100d5e67f9067b42546afb5e
