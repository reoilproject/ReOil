package com.example.reoil.store

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reoil.databinding.ActivityExchangeBinding

class ExchangeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExchangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExchangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            onBackPressed()
        }
    }
}