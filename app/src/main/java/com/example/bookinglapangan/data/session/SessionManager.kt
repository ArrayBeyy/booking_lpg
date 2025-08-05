package com.example.bookinglapangan.data.session

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)

    // Save full user data
    fun saveUser(email: String, password: String, name: String, cabang: String) {
        prefs.edit().apply {
            putString("USER_EMAIL", email)
            putString("USER_PASSWORD", password)
            putString("USER_NAME", name)
            putString("USER_CABANG", cabang)
            apply()
        }
    }

    fun saveUserId(userId: Int) {
        prefs.edit().putInt("USER_ID", userId).apply()
    }

    fun getUserId(): Int = prefs.getInt("USER_ID", 0)

    fun getEmail(): String? = prefs.getString("USER_EMAIL", null)
    fun getPassword(): String? = prefs.getString("USER_PASSWORD", null)
    fun getName(): String? = prefs.getString("USER_NAME", null)
    fun getCabang(): String? = prefs.getString("USER_CABANG", null)

    fun saveToken(token: String) {
        prefs.edit().putString("USER_TOKEN", token).apply()
    }

    fun getToken(): String? = prefs.getString("USER_TOKEN", null)

    fun saveName(name: String) {
        prefs.edit().putString("USER_NAME", name).apply()
    }

    fun saveEmail(email: String) {
        prefs.edit().putString("USER_EMAIL", email).apply()
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}

