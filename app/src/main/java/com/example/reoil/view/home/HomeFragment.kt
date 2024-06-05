package com.example.reoil.view.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reoil.R
import com.example.reoil.customview.CustomPageTransformer
import com.example.reoil.databinding.FragmentHomeBinding
import com.example.reoil.view.news.NewsActivity
import com.example.reoil.view.notification.NotificationActivity

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private lateinit var adapter: CarouselAdapter
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userName = arguments?.getString("USERNAME")
        val photoUrl = arguments?.getString("PHOTO_URL")

        binding!!.tvName.text = userName
        if (photoUrl != null) {
            Glide.with(this).load(photoUrl).into(binding!!.ivImage)
        }
        binding?.btnNotification?.setOnClickListener {
            val intent = Intent(context, NotificationActivity::class.java)
            startActivity(intent)
        }
        binding?.tvViewAll?.setOnClickListener {
            val intent = Intent(context, NewsActivity::class.java)
            startActivity(intent)
        }
        initCarousel()
    }

    private fun initCarousel() {
        val imageList = ArrayList<Int>()
        repeat(8) { imageList.add(R.drawable.carousel_img) }

        adapter = CarouselAdapter(imageList, binding!!.viewPager2)
        binding?.viewPager2?.apply {
            adapter = this@HomeFragment.adapter
            offscreenPageLimit = 3
            clipToPadding = true
            clipChildren = true
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            setPageTransformer(CustomPageTransformer())
        }
        setupAutoScroll()
    }

    private fun setupAutoScroll() {
        handler = Handler()
        runnable = Runnable {
            val currentItem = binding?.viewPager2?.currentItem ?: 0
            val itemCount = adapter.itemCount
            binding?.viewPager2?.currentItem = (currentItem + 1) % itemCount
            handler.postDelayed(runnable, 5000)
        }
        handler.postDelayed(runnable, 5000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
