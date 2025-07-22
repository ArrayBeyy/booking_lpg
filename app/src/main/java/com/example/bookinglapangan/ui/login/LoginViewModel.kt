package com.example.bookinglapangan.ui.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val sharedPref = context.getSharedPreferences("login_session", Context.MODE_PRIVATE)

    // LiveData untuk status login
    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    fun login(email: String, password: String) {
        // Simple validation (bisa diganti nanti saat pakai backend)
        if (email.isNotEmpty() && password.isNotEmpty()) {
            // Simpan login di SharedPreferences
            sharedPref.edit().putString("email", email).apply()
            _isLoggedIn.value = true
        } else {
            _isLoggedIn.value = false
        }
    }

    fun checkLoginStatus() {
        val isLogged = sharedPref.getString("email", null) != null
        _isLoggedIn.value = isLogged
    }

    fun logout() {
        sharedPref.edit().clear().apply()
        _isLoggedIn.value = false
    }

    fun getLoggedInEmail(): String? {
        return sharedPref.getString("email", null)
    }
}
