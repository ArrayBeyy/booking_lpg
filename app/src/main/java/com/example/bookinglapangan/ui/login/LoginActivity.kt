package com.example.bookinglapangan.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    companion object {
        const val EXTRA_REDIRECT_TO = "redirectTo"

        // Terima SEMUA varian string yang mungkin kamu pakai di tempat lain
        private val HISTORY_ALIASES = setOf(
            "history", "History", "bookingHistory", "BookingHistory",
            "HistoryBookingActivity", "BookingHistoryActivity"
        )
        private val PROFILE_ALIASES = setOf(
            "profile", "Profile", "userProfile", "UserProfile",
            "ProfileActivity"
        )
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
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
        firebaseAuth = FirebaseAuth.getInstance()

        // ✅ Jika SUDAH login, langsung arahkan sesuai redirectTo
        if (session.isLoggedIn()) {
            navigateAfterLogin()
            return
        }

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

                getSharedPreferences("session", MODE_PRIVATE)
                    .edit()
                    .putString("email", emailInput)
                    .apply()

                // ✅ Redirect sesuai tujuan (satu pintu)
                navigateAfterLogin()

            } else {
                Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
            }
        }

        // ✅ Pindah ke halaman register
        findViewById<TextView>(R.id.tvRegister).setOnClickListener {
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
            // signOut dulu biar selalu muncul chooser
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

                        val email = user.email ?: ""
                        getSharedPreferences("session", MODE_PRIVATE)
                            .edit()
                            .putString("email", email)
                            .apply()

                        Toast.makeText(this, "Selamat datang, ${user.displayName}", Toast.LENGTH_SHORT).show()
                    }

                    // ✅ Redirect sesuai tujuan (satu pintu)
                    navigateAfterLogin()

                } else {
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /** Satu pintu redirect + normalisasi alias */
    private fun navigateAfterLogin() {
        val raw = intent.getStringExtra(EXTRA_REDIRECT_TO)?.trim()
        Log.d("LoginActivity", "redirectTo raw intent extra = $raw")

        when {
            raw != null && HISTORY_ALIASES.contains(raw) -> {
                startActivity(Intent(this, HistoryBookingActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
            raw != null && PROFILE_ALIASES.contains(raw) -> {
                startActivity(Intent(this, ProfileActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
            else -> {
                // default fallback
                startActivity(Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
        finish()
    }
}
