package com.example.reoil.store

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.reoil.R
import com.example.reoil.api.ApiConfig
import com.example.reoil.databinding.FragmentAdminHomeBinding
import com.example.reoil.databinding.FragmentHomeBinding
import com.example.reoil.response.NewsItem
import com.example.reoil.view.detail.DetailNewsActivity
import com.example.reoil.view.home.CarouselAdapter
import com.example.reoil.view.home.HomeViewModele
import com.example.reoil.view.news.NewsActivity
import com.example.reoil.view.notification.NotificationActivity
import com.google.android.gms.maps.model.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminHomeFragment : Fragment() {
    private var _binding: FragmentAdminHomeBinding? = null
    private val userViewModel: HomeViewModele by viewModels()
    private val binding get() = _binding!!

    private lateinit var adapter: CarouselAdapter
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            binding.tvName.text = getString(R.string.welcome_user, profile?.username ?: "user")
            profile?.imageUrl?.let { imageUrl ->
                Glide.with(this).load(imageUrl).into(binding.ivImage)
            }
        }

        userViewModel.loadUserProfile()

        binding.btnNotification.setOnClickListener {
            val intent = Intent(context, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.btnTrade.setOnClickListener {
            val whatsappNumber = "+6281338491334"
            val whatsappMessage = "Halo, Reoil! Saya ingin menukar minyak jelantah saya. Berikut adalah detailnya:\n" +
                    "\n" +
                    "Nama Toko: Toko Sumber Makmur\n" +
                    "Jumlah Minyak Jelantah: 20 Liter\n" +
                    "\n" +
                    "Terima kasih!"
            val whatsappUrl = "https://wa.me/$whatsappNumber?text=$whatsappMessage"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(whatsappUrl)
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
            binding.let {
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
