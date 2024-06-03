package com.example.reoil

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.reoil.databinding.ActivityMainBinding
import com.example.reoil.utils.PreferencesHelper
import com.example.reoil.view.login.LoginActivity
import com.example.reoil.view.rewardpage.RewardPageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var preferencesHelper: PreferencesHelper
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        preferencesHelper = PreferencesHelper(this)

        val user = auth.currentUser

        updateUI(user)

        binding.btLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            preferencesHelper.clearLoginStatus()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.button.setOnClickListener {
            val intent = Intent(this, RewardPageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            binding.tvName.text = user.displayName

            user.photoUrl?.let { imageUrl ->
                Glide.with(this).load(imageUrl).into(binding.ivImage)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
