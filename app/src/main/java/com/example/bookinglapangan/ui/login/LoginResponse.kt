package com.example.bookinglapangan.ui.login

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: User?
)

data class User(
    val id: Int,
    val email: String,
    val name: String?
)