package com.example.reoil.view.register

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.reoil.R
import com.example.reoil.databinding.ActivityRegisterPartnerBinding
import com.example.reoil.utils.PreferencesHelper

class RegisterPartnerFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterPartnerBinding
    private lateinit var preferencesHelper: PreferencesHelper
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPartnerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesHelper = PreferencesHelper(this)

        binding.btGallery.setOnClickListener {
            openGallery()
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.btRegister.setOnClickListener {
            if (imageUri != null) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Registration Successful")
                builder.setMessage("Your registration has been submitted successfully. Please wait until the Reoil team contacts you.")
                builder.setPositiveButton("OK") { dialog, which ->
                    preferencesHelper.setPartnerRegistered(true)
                    finish()
                }
                val alertDialog = builder.show()
                val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setTextColor(resources.getColor(R.color.green))
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("Please select an image before registering.")
                builder.setPositiveButton("OK") { dialog, which -> }
                val alertDialog = builder.show()
                val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setTextColor(resources.getColor(R.color.green))
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data!= null) {
            imageUri = data.data
            binding.ivResultCamera.setImageURI(imageUri)
        }
    }
}