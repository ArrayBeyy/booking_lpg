package com.example.bookinglapangan.ui.lapangan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinglapangan.R
import com.example.bookinglapangan.data.pref.Lapangan
import com.example.bookinglapangan.data.pref.LapanganAdapter
import com.example.bookinglapangan.data.pref.LapanganViewModel

class LapanganActivity : AppCompatActivity() {
    private lateinit var viewModel: LapanganViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lapangan)

        recyclerView = findViewById(R.id.rvLapangan)
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this)[LapanganViewModel::class.java]

        viewModel.lapanganList.observe(this) { list ->
            recyclerView.adapter = LapanganAdapter(list)
        }

        viewModel.fetchLapangan()
    }
}
