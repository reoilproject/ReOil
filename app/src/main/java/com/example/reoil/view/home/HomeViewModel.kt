package com.example.reoil.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reoil.BuildConfig
import com.example.reoil.data.DefaultUserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModele : ViewModel() {
    val userProfile = MutableLiveData<DefaultUserProfile?>()

    fun loadUserProfile() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseDatabase.getInstance(BuildConfig.API_URL).reference.child("users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val profile = snapshot.getValue(DefaultUserProfile::class.java)
                    userProfile.postValue(profile)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Log error or handle cancellation
                }
            })
    }
}
