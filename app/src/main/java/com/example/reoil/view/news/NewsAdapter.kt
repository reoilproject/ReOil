package com.example.reoil.view.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reoil.databinding.ItemRowNewsBinding
import com.example.reoil.response.NewsItem

class NewsAdapter(private val onClick: (NewsItem) -> Unit) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private var newsList = listOf<NewsItem>()

    fun setNewsList(newsList: List<NewsItem>) {
        this.newsList = newsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemRowNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size

    class NewsViewHolder(private val binding: ItemRowNewsBinding, private val onClick: (NewsItem) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(newsItem: NewsItem) {
            binding.apply {
                tvItemName.text = newsItem.title
                tvItemFrom.text = newsItem.content
                Glide.with(imgItemPhoto.context)
                    .load(newsItem.imageUrl)
                    .into(imgItemPhoto)
                root.setOnClickListener {
                    onClick(newsItem)
                }
            }
        }
    }
}

