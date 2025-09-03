package com.example.bookinglapangan.ui.Kategori

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.bookinglapangan.R
import com.example.bookinglapangan.data.model.JamOperasional

class JamOperasionalFragment : Fragment(R.layout.fragment_jam_operasional) {
    companion object {
        fun newInstance(jam: JamOperasional?) = JamOperasionalFragment().apply {
            arguments = bundleOf("jam" to jam)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val jam = arguments?.getParcelable<JamOperasional>("jam")
        val tv = view.findViewById<TextView>(R.id.tvJam)
        fun row(h: String, list: List<String>) = "$h  ${if (list.isEmpty()) "-" else list.joinToString(", ")}"
        tv.text = listOf(
            row("Senin",  jam?.senin ?: emptyList()),
            row("Selasa", jam?.selasa ?: emptyList()),
            row("Rabu",   jam?.rabu ?: emptyList()),
            row("Kamis",  jam?.kamis ?: emptyList()),
            row("Jumat",  jam?.jumat ?: emptyList()),
            row("Sabtu",  jam?.sabtu ?: emptyList()),
            row("Minggu", jam?.minggu ?: emptyList())
        ).joinToString("\n")
    }
}
