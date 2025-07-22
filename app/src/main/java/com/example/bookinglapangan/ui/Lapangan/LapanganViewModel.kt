package com.example.bookinglapangan.ui.Lapangan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LapanganViewModel : ViewModel() {
    private val _lapanganList = MutableLiveData<List<Lapangan>>()
    val lapanganList: LiveData<List<Lapangan>> = _lapanganList

    fun fetchLapangan() {
        // Contoh data dummy sementara
        val dummyList = listOf(
            Lapangan(1, "Lapangan A"),
            Lapangan(2, "Lapangan B"),
            Lapangan(3, "Lapangan C")
        )
        _lapanganList.value = dummyList

        // _lapanganList.value = emptyList()
    }
}
