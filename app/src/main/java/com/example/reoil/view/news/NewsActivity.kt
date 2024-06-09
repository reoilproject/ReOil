package com.example.reoil.view.news

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reoil.api.ApiConfig
import com.example.reoil.databinding.ActivityNewsBinding
import com.example.reoil.response.NewsItem
import com.example.reoil.view.detail.DetailNewsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        setupRecyclerView()
        loadNews()
        setupView()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter { newsItem ->
            val intent = Intent(this@NewsActivity, DetailNewsActivity::class.java)
            intent.putExtra("NEWS_ITEM", newsItem)
            startActivity(intent)
        }
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@NewsActivity)
            adapter = newsAdapter
        }
    }

    private fun loadNews() {
        ApiConfig.getApiService().getNews().enqueue(object : Callback<Map<String, NewsItem>> {
            override fun onResponse(call: Call<Map<String, NewsItem>>, response: Response<Map<String, NewsItem>>) {
                if (response.isSuccessful) {
                    newsAdapter.setNewsList(response.body()?.values?.toList() ?: emptyList())
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<Map<String, NewsItem>>, t: Throwable) {
                showError(t.message ?: "An error occurred")
            }
        })
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

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
