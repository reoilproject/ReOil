package com.example.reoil

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.reoil.Onboarding.OnboardingActivity
import com.example.reoil.databinding.ActivitySplashScreenBinding
import com.example.reoil.utils.PreferencesHelper

class SplashScreen : AppCompatActivity() {

    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesHelper = PreferencesHelper(this)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            val isLoggedIn = preferencesHelper.getLoginStatus()

            val intent = if (isLoggedIn) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, OnboardingActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 3000)

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        binding.githubIcon.startAnimation(topAnimation)
        binding.githubIconText.startAnimation(bottomAnimation)
    }
}
