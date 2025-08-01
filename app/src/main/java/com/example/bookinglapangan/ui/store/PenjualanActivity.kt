package com.example.bookinglapangan.ui.store

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinglapangan.databinding.ActivityPenjualanBinding

class PenjualanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPenjualanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Contoh klik item (nantinya pakai RecyclerView)
        binding.btnBeliMakanan.setOnClickListener {
            Toast.makeText(this, "Makanan dibeli!", Toast.LENGTH_SHORT).show()
        }

        binding.btnBeliPeralatan.setOnClickListener {
            Toast.makeText(this, "Peralatan dibeli!", Toast.LENGTH_SHORT).show()
        }
    }
}
