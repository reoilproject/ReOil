package com.example.reoil.view.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val registrationStatus = MutableLiveData<Boolean>()
    val verificationEmailStatus = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            registrationStatus.value = true
                        } else {
                            verificationEmailStatus.value = false
                            errorMessage.value = verificationTask.exception?.message
                        }
                    }
                } else {
                    registrationStatus.value = false
                    errorMessage.value = task.exception?.message
                }
            }
    }
}
