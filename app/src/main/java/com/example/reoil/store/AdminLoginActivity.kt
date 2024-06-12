package com.example.reoil.store

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.reoil.R
import com.example.reoil.databinding.ActivityAdminLoginBinding
import com.example.reoil.main.PartnerActivity
import com.example.reoil.utils.PreferencesHelper
import com.example.reoil.view.login.LoginActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class AdminLoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityAdminLoginBinding
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        preferencesHelper = PreferencesHelper(this)

        animateTextViews()

        binding.buttonLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (email.isEmpty()) {
                binding.edtEmail.error = getString(R.string.error_email_required)
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.edtPassword.error = getString(R.string.error_password_required)
                return@setOnClickListener
            }

            showLoading(true)
            loginUser(email, password)
        }

        binding.userLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        binding.tvForgotpassword.setOnClickListener {
            val animation = ScaleAnimation(
                1f, 1.2f,
                1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )

            animation.duration = 150
            animation.fillAfter = true
            binding.tvForgotpassword.startAnimation(animation)

            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    val reverseAnimation = ScaleAnimation(
                        1.2f, 1.0f,
                        1.2f, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                    )

                    reverseAnimation.duration = 150
                    reverseAnimation.fillAfter = false
                    binding.tvForgotpassword.startAnimation(reverseAnimation)

                    Toast.makeText(this@AdminLoginActivity, "Coming soon!", Toast.LENGTH_SHORT).show()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
        }
    }

    private fun signIn() {
        showLoading(true)
        val credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = this@AdminLoginActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.d("Error", e.message.toString())
                showLoading(false)
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                        showLoading(false)
                    }
                } else {
                    Log.e(TAG, "Unexpected type of credential")
                    showLoading(false)
                }
            }

            else -> {
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }


    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    if (binding.checkBoxRememberMe.isChecked) {
                        preferencesHelper.setLoginStatus(true)
                    } else {
                        preferencesHelper.clearLoginStatus()
                    }
                    val intent = Intent(this, PartnerActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Login failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun animateTextViews() {
        binding.textView.animate().alpha(1f).setDuration(1000).setStartDelay(500)
        binding.textView2.animate().alpha(1f).setDuration(1000).setStartDelay(700)
        binding.textViewEmail.animate().alpha(1f).setDuration(1000).setStartDelay(900)
        binding.edtEmail.animate().alpha(1f).setDuration(1000).setStartDelay(1100)
        binding.textViewPassword.animate().alpha(1f).setDuration(1000).setStartDelay(1300)
        binding.edtPassword.animate().alpha(1f).setDuration(1000).setStartDelay(1500)
        binding.checkBoxRememberMe.animate().alpha(1f).setDuration(1000).setStartDelay(1700)
        binding.textViewRememberMe.animate().alpha(1f).setDuration(1000).setStartDelay(1900)
        binding.tvForgotpassword.animate().alpha(1f).setDuration(1000).setStartDelay(2100)
        binding.buttonLogin.animate().alpha(1f).setDuration(1000).setStartDelay(2300)
        binding.userLogin.animate().alpha(1f).setDuration(1000).setStartDelay(2600)
    }
}
