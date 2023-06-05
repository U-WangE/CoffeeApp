package com.uwange.coffeeapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.uwange.coffeeapp.R

class ImageSliderAdapter(
    private val context: Context,
    private val imageUrls: List<Task<Uri>>
) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_slider, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrlTask = imageUrls[position]

        imageUrlTask.addOnSuccessListener { imageUrl ->
            Glide.with(context)
                .load(imageUrl)
                .into(holder.imageView)
        }.addOnFailureListener { exception ->
            // Handle failure to fetch image
        }
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}