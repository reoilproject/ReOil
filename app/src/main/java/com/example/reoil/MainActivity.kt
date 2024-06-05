package com.example.reoil

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.reoil.databinding.ActivityMainBinding
import com.example.reoil.utils.PreferencesHelper
import com.example.reoil.utils.getImageUri
import com.example.reoil.utils.uriToFile
import com.example.reoil.view.home.HomeFragment
import com.example.reoil.view.login.LoginActivity
import com.example.reoil.view.rewardpage.RewardPageActivity
import com.google.firebase.auth.FirebaseAuth
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var preferencesHelper: PreferencesHelper
    private lateinit var binding: ActivityMainBinding
    private var getFile: File? = null
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        preferencesHelper = PreferencesHelper(this)

        val user = auth.currentUser

        val homeFragment = HomeFragment().apply {
            arguments = Bundle().apply {
                if (user != null) {
                    putString("USERNAME", "Welcome, ${user.displayName}")
                }
                if (user != null) {
                    putString("PHOTO_URL", user.photoUrl.toString())
                }
            }
        }

        replaceFragment(homeFragment)
        setupView()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.scan -> {
                    startCamera()
                }
                R.id.map -> replaceFragment(HomeFragment())
                R.id.user -> replaceFragment(HomeFragment())
                else->{

                }

            }
            true

        }

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

    private fun startCamera() {
        currentImageUri = getImageUri(this) // Dapatkan URI untuk menyimpan foto
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            getFile =
                uriToFile(currentImageUri!!, this) // Anda perlu implementasi fungsi `uriToFile`
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
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
