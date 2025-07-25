package com.example.bookinglapangan.data.pref

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookinglapangan.data.api.ApiClient
import kotlinx.coroutines.launch

class LapanganViewModel : ViewModel() {
    private val _lapanganList = MutableLiveData<List<Lapangan>>()
    val lapanganList: LiveData<List<Lapangan>> = _lapanganList

    fun fetchLapangan() {
        viewModelScope.launch {
            try {
                val result = ApiClient.lapanganService.getLapangan()
                _lapanganList.value = result
            } catch (e: Exception) {
                Log.e("LapanganVM", "Gagal ambil data: ${e.message}")
            }
        }
    }
}
