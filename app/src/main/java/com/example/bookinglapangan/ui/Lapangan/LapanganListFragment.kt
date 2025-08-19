package com.example.bookinglapangan.ui.lapangan

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookinglapangan.R
import com.example.bookinglapangan.databinding.FragmentLapanganListBinding

// PENTING: pakai data class Lapangan yang sudah ada di paket ini:
import com.example.bookinglapangan.ui.Lapangan.Lapangan

class LapanganListFragment : Fragment(R.layout.fragment_lapangan_list) {

    private var _binding: FragmentLapanganListBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLapanganListBinding.bind(view)

        val categoryId = arguments?.getString("categoryId") ?: ""
        val categoryName = arguments?.getString("categoryName") ?: "Lapangan"
        binding.tvTitle.text = categoryName

        // ==========
        // FIX DI SINI — gunakan List<Lapangan>, bukan List<LapanganItem>
        // ==========
        val items: List<Lapangan> =
            if (categoryId == "bulu_tangkis") {
                listOf(
                    Lapangan(
                        id = 1,                      // ← Int, bukan "lap_pengki"
                        nama = "Lapanggan Surabaya",
                        lokasi = "Isi alamat nanti",
                        harga_per_jam = 50000 // kalau tipe di model Double: 50000.0
                    )
                )
            } else {
                emptyList()
            }

        binding.rvLapangan.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLapangan.adapter = LapanganAdapter(items) { item ->
            // Buka LapanganActivity saat item dipilih
            val intent = Intent(requireContext(), LapanganActivity::class.java)
            intent.putExtra("lapangan_nama", item.nama)
            intent.putExtra("lapangan_lokasi", item.lokasi)
            intent.putExtra("harga_per_jam", item.harga_per_jam)
            startActivity(intent)
        }

        // Biar tidak ketutup bottom bar kalau list panjang
        if (binding.rvLapangan.paddingBottom < dp(140)) {
            binding.rvLapangan.setPadding(
                binding.rvLapangan.paddingLeft,
                binding.rvLapangan.paddingTop,
                binding.rvLapangan.paddingRight,
                dp(140)
            )
        }
        binding.rvLapangan.clipToPadding = false
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
