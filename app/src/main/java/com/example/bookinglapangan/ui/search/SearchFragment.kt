package com.example.bookinglapangan.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.bookinglapangan.R
import com.example.bookinglapangan.databinding.FragmentSearchBinding

class SearchFragment : Fragment(R.layout.fragment_search) {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        val q = arguments?.getString("query").orEmpty()
        binding.tvQuery.text = if (q.isEmpty()) "Cari sesuatu…" else "Hasil untuk: $q"
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
