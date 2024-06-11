package com.example.reoil.view.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.reoil.BuildConfig
import com.example.reoil.data.DefaultUserProfile
import com.example.reoil.data.UserProfile
import com.example.reoil.databinding.ActivityProfileBinding
import com.example.reoil.main.MainActivity
import com.example.reoil.utils.PreferencesHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private var imageUrl: String? = null
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance(BuildConfig.API_URL)
        storage = FirebaseStorage.getInstance()
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
            if (imageUrl != null) {
                saveUserProfile(imageUrl!!)
            } else {
                Toast.makeText(this, "Please choose and upload an image first", Toast.LENGTH_SHORT).show()
            }
        }

        loadData()
        setupView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                binding.ppEdit.setImageURI(uri)
                uploadImage(uri)
            }
        }
    }

    private fun uploadImage(imageUri: Uri) {
        val userId = auth.currentUser?.uid ?: return Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()

        val storageRef = storage.getReference("profile/$userId")
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    imageUrl = uri.toString()
                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadData() {
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = user.uid
        binding.tvUsernameProfile.text = user.email

        database.reference.child("users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.d("ProfileActivity", "DataSnapshot: $dataSnapshot")
                    val userProfile = dataSnapshot.getValue(DefaultUserProfile::class.java)
                    Log.d("ProfileActivity", "UserProfile: $userProfile")
                    userProfile?.let {
                        binding.usernameProfile.setText(it.username)
                        binding.phoneProfile.setText(it.phone)
                        binding.addressProfile.setText(it.address)
                        // Set image if URL is available
                        if (!it.imageUrl.isNullOrEmpty()) {
                            Glide.with(this@ProfileActivity).load(it.imageUrl).into(binding.ppEdit)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@ProfileActivity, "Data could not be retrieved", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun saveUserProfile(imageUrl: String) {
        val username = binding.usernameProfile.text.toString()
        val phone = binding.phoneProfile.text.toString()
        val address = binding.addressProfile.text.toString()

        val userProfile: UserProfile = DefaultUserProfile(username, phone, address, imageUrl)

        database.reference.child("users").child(auth.currentUser!!.uid).setValue(userProfile)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save profile", Toast.LENGTH_SHORT).show()
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
