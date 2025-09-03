package com.example.bookinglapangan.ui.lapangan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookinglapangan.R
import com.example.bookinglapangan.data.api.RetrofitClient
import com.example.bookinglapangan.data.repository.LapanganRepository
import com.example.bookinglapangan.databinding.FragmentLapanganListBinding
import kotlinx.coroutines.launch

class LapanganListFragment : Fragment(R.layout.fragment_lapangan_list) {

    private var _binding: FragmentLapanganListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: LapanganAdapter
    private val repo by lazy { LapanganRepository(RetrofitClient.apiService) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLapanganListBinding.bind(view)

        val slug = arguments?.getString("categoryId") ?: ""
        val categoryName = arguments?.getString("categoryName") ?: "Lapangan"
        binding.tvTitle.text = categoryName

        // ✅ Buat adapter TANPA list (hanya lambda)
        adapter = LapanganAdapter { item ->
            val i = Intent(requireContext(), LapanganActivity::class.java).apply {
                putExtra("lapangan_id", item.id)
                putExtra("lapangan_nama", item.nama)
                putExtra("harga_per_jam", item.harga_per_jam)
                putExtra("kota", item.kota)
            }
            startActivity(i)
        }

        binding.rvLapangan.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLapangan.adapter = adapter
        binding.rvLapangan.setHasFixedSize(true)

        loadData(slug)

        // padding bawah biar aman
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

    private fun loadData(slug: String) {
        showLoading(true)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // ✅ List<LapanganItem> dari repo
                val list = repo.fetchByKategoriId(kategoriId = 3, q = null)
                adapter.submitList(list) // ✅ masukkan data DI SINI
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal memuat: ${e.message}", Toast.LENGTH_LONG).show()
                adapter.submitList(emptyList())
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(show: Boolean) = with(binding) {
        // Baris ini TIDAK terkait error type mismatch; aman tetap seperti ini
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        rvLapangan.visibility = if (show) View.INVISIBLE else View.VISIBLE
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvLapangan.adapter = null
        _binding = null
    }
}
