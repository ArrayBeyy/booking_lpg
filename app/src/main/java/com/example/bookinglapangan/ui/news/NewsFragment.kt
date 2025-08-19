package com.example.bookinglapangan.ui.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.bookinglapangan.R
import com.example.bookinglapangan.databinding.FragmentNewsBinding

class NewsFragment : Fragment(R.layout.fragment_news) {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewsBinding.bind(view)
        // TODO: isi feed news di sini
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
