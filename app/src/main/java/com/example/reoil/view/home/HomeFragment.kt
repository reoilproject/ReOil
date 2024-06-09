package com.example.reoil.view.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.reoil.api.ApiConfig
import com.example.reoil.databinding.FragmentHomeBinding
import com.example.reoil.main.UserViewModel
import com.example.reoil.response.NewsItem
import com.example.reoil.view.detail.DetailNewsActivity
import com.example.reoil.view.news.NewsActivity
import com.example.reoil.view.notification.NotificationActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CarouselAdapter
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.username?.let {
            binding.tvName.text = it
        }

        userViewModel.photoUrl?.let {
            Glide.with(this).load(it).into(binding.ivImage)
        }

        binding.btnNotification.setOnClickListener {
            val intent = Intent(context, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.tvViewAll.setOnClickListener {
            val intent = Intent(context, NewsActivity::class.java)
            startActivity(intent)
        }

        initCarousel()
    }

    private fun initCarousel() {
        ApiConfig.getApiService().getNews().enqueue(object : Callback<Map<String, NewsItem>> {
            override fun onResponse(call: Call<Map<String, NewsItem>>, response: Response<Map<String, NewsItem>>) {
                if (response.isSuccessful) {
                    val newsItems = response.body()?.values?.toList() ?: emptyList()
                    adapter = CarouselAdapter(newsItems) { newsItem ->
                        val intent = Intent(context, DetailNewsActivity::class.java)
                        intent.putExtra("NEWS_ITEM", newsItem)
                        startActivity(intent)
                    }
                    binding.viewPager2.adapter = adapter
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<Map<String, NewsItem>>, t: Throwable) {
                showError(t.message ?: "An error occurred")
            }
        })
        setupAutoScroll()
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


    private fun setupAutoScroll() {
        handler = Handler()
        runnable = Runnable {
            binding?.let {
                val currentItem = it.viewPager2.currentItem
                val itemCount = adapter.itemCount
                it.viewPager2.currentItem = (currentItem + 1) % itemCount
                handler.postDelayed(runnable, 5000)
            }
        }
        handler.postDelayed(runnable, 5000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
        _binding = null
    }
}
