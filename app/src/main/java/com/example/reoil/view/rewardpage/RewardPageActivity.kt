package com.example.reoil.view.rewardpage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reoil.databinding.ActivityRewardPageBinding

class RewardPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRewardPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
}