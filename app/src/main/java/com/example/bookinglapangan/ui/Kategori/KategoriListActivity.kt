/*package com.example.bookinglapangan.ui.Kategori

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookinglapangan.ui.Lapangan.LapanganByKategoriViewModel
import com.example.bookinglapangan.ui.lapangan.LapanganAdapter

class KategoriListActivity : AppCompatActivity() {

    private lateinit var adapter: LapanganAdapter
    private val vm: LapanganByKategoriViewModel by viewModels

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityKategoriListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = LapanganAdapter { item ->
            startActivity(Intent(this, DetilTempatActivity::class.java).apply {
                putExtra("lapangan", item)
            })
        }
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(this)

        // Search di bagian atas
        binding.etSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                vm.load(kategoriSlug = "tenis-meja", q = v.text.toString().trim())
                true
            } else false
        }

        lifecycleScope.launchWhenStarted {
            vm.state.collect { s ->
                binding.progress.isVisible = s.loading
                binding.viewError.isVisible = s.error != null
                adapter.submitList(s.items)
            }
        }

        vm.load(kategoriSlug = "tenis-meja")
    }
}*/
