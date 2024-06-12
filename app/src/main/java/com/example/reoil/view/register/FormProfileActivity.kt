package com.example.reoil.view.register

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.reoil.BuildConfig
import com.example.reoil.data.DefaultUserProfile
import com.example.reoil.data.UserProfile
import com.example.reoil.databinding.ActivityFormProfileBinding
import com.example.reoil.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class FormProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormProfileBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance(BuildConfig.API_URL)
        storage = FirebaseStorage.getInstance()

        binding.btnSave.setOnClickListener {
            if (imageUrl != null) {
                saveUserProfile(imageUrl!!)
            } else {
                Toast.makeText(this, "Please choose and upload an image first", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvUsernameProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }
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

    companion object {
        const val IMAGE_PICK_CODE = 1000
    }
}

