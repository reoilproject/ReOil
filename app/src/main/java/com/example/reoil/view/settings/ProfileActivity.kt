package com.example.reoil.view.settings

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.reoil.databinding.ActivityProfileBinding
import com.example.reoil.utils.PreferencesHelper
import com.example.reoil.view.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var preferencesHelper: PreferencesHelper
    private val profileViewModel: ProfileViewModel by viewModels { ViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesHelper = PreferencesHelper(this)

        binding.btBack.setOnClickListener {
            onBackPressed()
        }

        binding.usernameProfile.hint = preferencesHelper.getUsername()

        binding.btnGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        binding.btnSave.setOnClickListener {
            val username = binding.usernameProfile.text.toString()
            val phone = binding.phoneProfile.text.toString()
            val address = binding.addressProfile.text.toString()
            val imageUrl = profileViewModel.imageUrl.value ?: profileViewModel.userProfile.value?.imageUrl ?: ""
            profileViewModel.saveUserProfile(username, phone, address, imageUrl)
        }

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        binding.tvUsernameProfile.text = user.email

        profileViewModel.userProfile.observe(this) { profile ->
            binding.usernameProfile.setText(profile?.username)
            binding.phoneProfile.setText(profile?.phone)
            binding.addressProfile.setText(profile?.address)
            profile?.imageUrl?.let {
                Glide.with(this).load(it).into(binding.ppEdit)
            }
        }

        profileViewModel.imageUrl.observe(this) { imageUrl ->
            if (imageUrl != null) {
                Glide.with(this).load(imageUrl).into(binding.ppEdit)
            }
        }

        profileViewModel.loadUserProfile()
        setupView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                binding.ppEdit.setImageURI(uri)
                profileViewModel.uploadImage(uri)
            }
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

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}
