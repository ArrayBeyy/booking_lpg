package com.example.bookinglapangan.ui.login

data class LoginResponse(
    val token: String,
    val user: User
)

data class User(
    val id: Int,
    val name: String,
    val email: String
)