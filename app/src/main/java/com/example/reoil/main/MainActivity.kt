package com.example.reoil.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.reoil.R
import com.example.reoil.databinding.ActivityMainBinding
import com.example.reoil.utils.PreferencesHelper
import com.example.reoil.view.home.HomeFragment
import com.example.reoil.view.login.LoginActivity
import com.example.reoil.view.map.MapFragment
import com.example.reoil.view.scan.ScanFragment
import com.example.reoil.view.settings.SettingsFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var preferencesHelper: PreferencesHelper
    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        preferencesHelper = PreferencesHelper(this)

        val user = auth.currentUser

        if (user != null) {
            userViewModel.username = "Welcome, ${user.displayName}"
            userViewModel.photoUrl = user.photoUrl.toString()
        }

        val homeFragment = HomeFragment()

        replaceFragment(homeFragment)
        setupView()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.scan -> replaceFragment(ScanFragment())
                R.id.map -> replaceFragment(MapFragment())
                R.id.user -> replaceFragment(SettingsFragment())
                else -> {
                    // Handle other cases if necessary
                }
            }
            true
        }
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

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
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
