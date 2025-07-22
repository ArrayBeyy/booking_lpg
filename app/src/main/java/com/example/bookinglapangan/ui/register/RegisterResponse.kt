package com.example.bookinglapangan.ui.register

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val user: User?
)

data class User(
    val id: Int,
    val phone_number: String,
    val name: String?,
    val role: String,
)
