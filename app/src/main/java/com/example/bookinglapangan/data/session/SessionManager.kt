package com.example.bookinglapangan.data.session

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)

    fun saveUser(email: String, password: String, name: String) {
        prefs.edit().apply {
            putString("email", email)
            putString("password", password)
            putString("name", name)
            apply()
        }
    }

    fun getEmail(): String? = prefs.getString("email", null)
    fun getPassword(): String? = prefs.getString("password", null)
    fun getName(): String? = prefs.getString("name", null)

    fun saveToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    fun getToken(): String? = prefs.getString("token", null)

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
