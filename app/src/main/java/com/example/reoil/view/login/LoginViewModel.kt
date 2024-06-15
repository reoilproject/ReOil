package com.example.reoil.view.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.reoil.utils.PreferencesHelper
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val preferencesHelper = PreferencesHelper(application)

    fun loginUser(email: String, password: String, rememberMe: Boolean, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (rememberMe) {
                        preferencesHelper.setLoginStatus(true)
                    } else {
                        preferencesHelper.clearLoginStatus()
                    }
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    fun firebaseAuthWithGoogle(idToken: String, rememberMe: Boolean, onComplete: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (rememberMe) {
                        preferencesHelper.setLoginStatus(true)
                    } else {
                        preferencesHelper.clearLoginStatus()
                    }
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    fun handleSignIn(credential: GoogleIdTokenCredential, onComplete: (Boolean, String?) -> Unit) {
        try {
            val idToken = credential.idToken
            firebaseAuthWithGoogle(idToken, true, onComplete)
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("LoginViewModel", "Received an invalid google id token response", e)
            onComplete(false, e.message)
        }
    }
}
