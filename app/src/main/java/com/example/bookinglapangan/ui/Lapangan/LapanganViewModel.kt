package com.example.bookinglapangan.ui.Lapangan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookinglapangan.data.api.RetrofitClient
import kotlinx.coroutines.launch

class LapanganViewModel : ViewModel() {
    private val _lapanganList = MutableLiveData<List<Lapangan>>()
    val lapanganList: LiveData<List<Lapangan>> get() = _lapanganList

    fun fetchLapangan(jenis: String? = null, sportId: Int? = null) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getLapangans()
                if (response.isSuccessful && response.body()?.status == "success") {
                    _lapanganList.postValue(response.body()?.data ?: emptyList())
                } else {
                    _lapanganList.postValue(emptyList())
                }
            } catch (e: Exception) {
                _lapanganList.postValue(emptyList())
            }
        }
    }
}

