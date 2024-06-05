package com.example.reoil.view.news

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reoil.databinding.ItemRowNewsBinding
import com.example.reoil.view.detail.DetailNewsActivity

class NewsAdapter(
    private val context: Context,
    private val imageIds: Array<Int>,
    private val titles: Array<String>,
    private val dates: Array<String>,
    private val descriptions: Array<String>
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemRowNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(imageIds[position], titles[position], dates[position], descriptions[position])
    }

    override fun getItemCount(): Int = titles.size

    inner class NewsViewHolder(private val binding: ItemRowNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageId: Int, title: String, date: String, description: String) {
            binding.imgItemPhoto.setImageResource(imageId)
            binding.tvItemName.text = title
            binding.tvItemFrom.text = date
            binding.cardView.setOnClickListener {
                val intent = Intent(context, DetailNewsActivity::class.java).apply {
                    putExtra("EXTRA_TITLE", title)
                    putExtra("EXTRA_DESCRIPTION", description)
                    putExtra("EXTRA_DATE", date)
                    putExtra("EXTRA_IMAGE_ID", imageId)
                }
                context.startActivity(intent)
            }
        }
    }
}
