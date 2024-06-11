package com.example.reoil.view.settings

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.reoil.BuildConfig
import com.example.reoil.data.DefaultUserProfile
import com.example.reoil.data.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val _userProfile = MutableLiveData<DefaultUserProfile?>()
    val userProfile: LiveData<DefaultUserProfile?> = _userProfile

    private val _imageUrl = MutableLiveData<String?>()
    val imageUrl: LiveData<String?> = _imageUrl

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(BuildConfig.API_URL)
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    fun loadUserProfile() {
        val userId = auth.currentUser?.uid ?: return
        database.reference.child("users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val profile = snapshot.getValue(DefaultUserProfile::class.java)
                    _userProfile.postValue(profile)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Log error or handle cancellation
                }
            })
    }

    fun uploadImage(imageUri: Uri) {
        val userId = auth.currentUser?.uid ?: return

        val storageRef = storage.getReference("profile/$userId")
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    _imageUrl.postValue(uri.toString())
                }
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun saveUserProfile(username: String, phone: String, address: String, imageUrl: String) {
        val userId = auth.currentUser?.uid ?: return
        val userProfile: UserProfile = DefaultUserProfile(username, phone, address, imageUrl)

        database.reference.child("users").child(userId).setValue(userProfile)
            .addOnSuccessListener {
                Toast.makeText(getApplication(), "Profile saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(getApplication(), "Failed to save profile", Toast.LENGTH_SHORT).show()
            }
    }
}
