package com.example.bookinglapangan.ui.lapangan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinglapangan.R
import com.example.bookinglapangan.ui.Lapangan.Lapangan
import com.example.bookinglapangan.ui.Lapangan.LapanganViewModel

class LapanganActivity : AppCompatActivity() {
    private lateinit var viewModel: LapanganViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lapangan)

        recyclerView = findViewById(R.id.rvLapangan)
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this)[LapanganViewModel::class.java]

        viewModel.lapanganList.observe(this) { list: List<Lapangan> ->
            recyclerView.adapter = object : RecyclerView.Adapter<LapanganViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapanganViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_lapangan, parent, false)
                    return LapanganViewHolder(view)
                }

                override fun onBindViewHolder(holder: LapanganViewHolder, position: Int) {
                    val lapangan = list[position]
                    holder.bind(lapangan)
                }

                override fun getItemCount(): Int = list.size
            }
        }

        viewModel.fetchLapangan()
    }

    class LapanganViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNama: TextView = itemView.findViewById(R.id.tvNamaLapangan)

        fun bind(lapangan: Lapangan) {
            tvNama.text = lapangan.nama
        }
    }
}