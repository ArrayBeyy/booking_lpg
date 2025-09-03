package com.example.bookinglapangan.ui.Kategori

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class SimpleImagePagerAdapter(
    images: List<String>?
) : RecyclerView.Adapter<SimpleImagePagerAdapter.VH>() {

    private val items: List<String> = images ?: emptyList()

    inner class VH(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val iv = ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
            adjustViewBounds = true
        }
        return VH(iv)
    }

    override fun getItemCount(): Int = if (items.isEmpty()) 1 else items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val url = items.getOrNull(position)
        if (url.isNullOrBlank()) {
            // Optional: set placeholder bawaan, kalau punya drawable:
            // holder.imageView.setImageResource(R.drawable.placeholder)
            holder.imageView.setImageDrawable(null)
        } else {
            Glide.with(holder.imageView)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView)
        }
    }
}
