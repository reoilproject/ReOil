package com.example.reoil.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.reoil.databinding.ActivityDetailNewsBinding
import com.example.reoil.response.NewsItem

class DetailNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        val newsItem = intent.getParcelableExtra<NewsItem>("NEWS_ITEM")
        newsItem?.let {
            showDetails(it)
        }
    }

    private fun showDetails(newsItem: NewsItem) {
        Glide.with(this)
            .load(newsItem.imageUrl)
            .into(binding.ivDetailPhoto)
        binding.tvDetailName.text = newsItem.title
        binding.tvDetailDescription.text = newsItem.content
    }
}
