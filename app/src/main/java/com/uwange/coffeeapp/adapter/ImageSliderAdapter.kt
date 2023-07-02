package com.uwange.coffeeapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.tasks.Task
import com.uwange.coffeeapp.R

class ImageSliderAdapter(
    private val context: Context,
    private val imageUrls: List<Task<Uri>>,
    private val progressBar: ProgressBar
) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_slider, parent, false)
        return ImageViewHolder(view)
    }
    
    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrlTask = imageUrls[position]
        progressBar.visibility = View.VISIBLE

        imageUrlTask.addOnSuccessListener { imageUrl ->
            Glide.with(context)
                .load(imageUrl)
                .thumbnail(0.1f)
                .error(R.drawable.ic_broken_image)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        holder.imageView.scaleType = ImageView.ScaleType.CENTER
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        holder.imageView.setImageDrawable(resource)
                        return false
                    }
                })
                .into(holder.imageView)
        }.addOnFailureListener { exception ->
            // Handle failure to fetch image
        }
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById<ImageView?>(R.id.imageView).apply {
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
    }
}