package com.example.bookinglapangan.ui.lapangan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookinglapangan.data.repository.LapanganRepository

class LapanganByKategoriVMFactory(
    private val repo: LapanganRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LapanganByKategoriViewModel::class.java)) {
            return LapanganByKategoriViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
