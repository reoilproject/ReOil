package com.example.reoil.view.result

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reoil.databinding.ActivityResultBinding
import com.example.reoil.main.MainActivity
import com.example.reoil.view.map.MapActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val imageUri = intent.getStringExtra("imageUri")
        val result = intent.getStringExtra("result")

        if (imageUri != null && result != null) {
            val inputStream = contentResolver.openInputStream(Uri.parse(imageUri))
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.ivResultCamera.setImageBitmap(bitmap)
            binding.tvResult.text = result

            binding.btBackHome.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            binding.backButton.setOnClickListener {
                onBackPressed()
            }

            binding.btTrade.setOnClickListener {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
