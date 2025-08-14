package com.example.bookinglapangan.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinglapangan.MainActivity
import com.example.bookinglapangan.R
import com.example.bookinglapangan.data.session.SessionManager
import com.example.bookinglapangan.databinding.ActivityLoginBinding
import com.example.bookinglapangan.ui.booking.HistoryBookingActivity
import com.example.bookinglapangan.ui.profile.ProfileActivity
import com.example.bookinglapangan.ui.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Login gagal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val session = SessionManager(this)

        // ✅ Jika user sudah login, langsung masuk ke MainActivity
        if (session.isLoggedIn()) {
            val redirectTo = intent.getStringExtra("redirectTo")
            if (redirectTo == "history") {
                startActivity(Intent(this, HistoryBookingActivity::class.java))
            } else if (redirectTo == "profile") {
                startActivity(Intent(this, ProfileActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
            return
        }


        firebaseAuth = FirebaseAuth.getInstance()

        // ✅ Login manual
        binding.btnLogin.setOnClickListener {
            val emailInput = binding.etEmail.text.toString()
            val passwordInput = binding.etPassword.text.toString()

            val savedEmail = session.getEmail()
            val savedPassword = session.getPassword()

            if (emailInput == savedEmail && passwordInput == savedPassword) {
                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()

                session.setLoginStatus(true)

                if (session.getName().isNullOrEmpty()) {
                    session.saveName("Pengguna")
                    session.saveEmail(emailInput)
                }

                val redirectTo = intent.getStringExtra("redirectTo")
                if (redirectTo == "history") {
                    startActivity(Intent(this, HistoryBookingActivity::class.java))
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                finish()

            } else {
                Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
            }
        }

        // ✅ Pindah ke halaman register
        val tvRegister = findViewById<TextView>(R.id.tvRegister)
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // ✅ Konfigurasi Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // ✅ Login Google
        binding.btnGoogleSignIn.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener {
                signInWithGoogle()
            }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        val session = SessionManager(this)
                        session.saveName(user.displayName ?: "Pengguna")
                        session.saveEmail(user.email ?: "")
                        session.setLoginStatus(true)

                        Toast.makeText(this, "Selamat datang, ${user.displayName}", Toast.LENGTH_SHORT).show()
                    }

                    val redirectTo = intent.getStringExtra("redirectTo")
                    if (redirectTo == "history") {
                        startActivity(Intent(this, HistoryBookingActivity::class.java))
                    } else {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    finish()

                } else {
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
