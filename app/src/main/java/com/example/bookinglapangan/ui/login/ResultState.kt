package com.example.bookinglapangan.ui.login

sealed class ResultState {
    object Loading : ResultState()
    data class Success(val message: String) : ResultState()
    data class Error(val error: String) : ResultState()
}