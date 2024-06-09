package com.example.reoil.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reoil.R
import com.example.reoil.response.NewsItem

class CarouselAdapter(
    private val newsList: List<NewsItem>,
    private val onClick: (NewsItem) -> Unit
) : RecyclerView.Adapter<CarouselAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img_photos)
        val textView: TextView = itemView.findViewById(R.id.tv_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carousel_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val newsItem = newsList[position]
        Glide.with(holder.imageView.context)
            .load(newsItem.imageUrl)
            .into(holder.imageView)
        holder.textView.text = newsItem.title
        holder.itemView.setOnClickListener {
            onClick(newsItem)
        }
    }

    override fun getItemCount(): Int = newsList.size
}