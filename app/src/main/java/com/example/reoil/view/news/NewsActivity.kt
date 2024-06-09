package com.example.reoil.view.news

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reoil.api.ApiConfig
import com.example.reoil.databinding.ActivityNewsBinding
import com.example.reoil.response.NewsItem
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
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
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

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
