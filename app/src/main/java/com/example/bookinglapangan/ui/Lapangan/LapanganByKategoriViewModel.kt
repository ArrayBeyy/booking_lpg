package com.example.bookinglapangan.ui.lapangan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookinglapangan.data.model.LapanganItem
import com.example.bookinglapangan.data.repository.LapanganRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LapanganByKategoriViewModel(
    private val repo: LapanganRepository
) : ViewModel() {

    data class UiState(
        val loading: Boolean = false,
        val items: List<LapanganItem> = emptyList(),
        val error: String? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun load(slug: String, q: String? = null) {
        viewModelScope.launch {
            // set loading
            _state.value = _state.value.copy(loading = true, error = null)

            runCatching { repo.fetchByKategori(slug, q) }
                .onSuccess { list ->
                    // success -> loading false, tampilkan items
                    _state.value = UiState(items = list, loading = false)
                }
                .onFailure { e ->
                    // error -> loading false, tampilkan pesan
                    _state.value = UiState(error = e.message ?: "Gagal memuat", loading = false)
                }
        }
    }
}
