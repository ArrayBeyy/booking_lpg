package com.example.bookinglapangan.ui.home

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookinglapangan.R
import com.example.bookinglapangan.data.fragmen_logic.SportCategory
import com.example.bookinglapangan.databinding.FragmentHomeBinding
import com.example.bookinglapangan.ui.adapter.BannerAdapter
import com.example.bookinglapangan.ui.adapter.CategoryAdapter
import com.example.bookinglapangan.ui.adapter.GridSpacingItemDecoration
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        // === Search ===
        binding.etSearch.setOnEditorActionListener { v, actionId, _ ->
            val q = v.text?.toString()?.trim().orEmpty()
            if (actionId == EditorInfo.IME_ACTION_SEARCH && q.isNotEmpty()) {
                // pastikan id "searchFragment" ada di nav_graph
                findNavController().navigate(
                    R.id.searchFragment,
                    bundleOf("query" to q)
                )
                true
            } else false
        }

        // === Banner / Carousel ===
        val banners = listOf(
            R.drawable.banner_padel,
            R.drawable.banner_badminton,
            R.drawable.banner_swim
        )
        val bannerAdapter = BannerAdapter(banners)
        binding.vpBanner.adapter = bannerAdapter
        TabLayoutMediator(binding.tabsIndicator, binding.vpBanner) { _, _ -> }.attach()

        // === Kategori (dummy) ===
        val categories = listOf(
            SportCategory("tenis_meja", "Tenis Meja", R.drawable.cat_tenis_meja),
            SportCategory("padel", "Padel", R.drawable.cat_padel),
            SportCategory("bulu_tangkis", "Bulu Tangkis", R.drawable.cat_badminton),
            SportCategory("futsal", "Futsal", R.drawable.cat_futsal),
            SportCategory("renang", "Renang", R.drawable.cat_renang),
            SportCategory("tennis", "Tennis", R.drawable.cat_tenis)
        )

        val catAdapter = CategoryAdapter(categories) { item ->
            // buka daftar lapangan sesuai kategori
            findNavController().navigate(
                R.id.lapanganListFragment,
                bundleOf("categoryId" to item.id, "categoryName" to item.title)
            )
        }
        binding.rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvCategories.adapter = catAdapter

        if (binding.rvCategories.itemDecorationCount == 0) {
            binding.rvCategories.addItemDecoration(
                GridSpacingItemDecoration(spanCount = 2, spacingPx = dp(12), includeEdge = true)
            )
        }

        binding.rvCategories.setPadding(
            binding.rvCategories.paddingLeft,
            binding.rvCategories.paddingTop,
            binding.rvCategories.paddingRight,
            dp(160) // 150–180dp aman, silakan sesuaikan
        )
        binding.rvCategories.clipToPadding = false
    }

    private fun dp(v: Int): Int =
        (v * resources.displayMetrics.density).toInt()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
