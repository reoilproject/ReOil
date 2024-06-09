package com.example.reoil.view.detail

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
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

        val newsItem = intent.getParcelableExtra<NewsItem>("NEWS_ITEM")
        newsItem?.let {
            showDetails(it)
            setupView()
        }
    }

    private fun showDetails(newsItem: NewsItem) {
        Glide.with(this)
            .load(newsItem.imageUrl)
            .into(binding.ivDetailPhoto)
        binding.tvDetailName.text = newsItem.title
        binding.tvDetailDescription.text = newsItem.content
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

}
