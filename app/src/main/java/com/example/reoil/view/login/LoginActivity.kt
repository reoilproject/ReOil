package com.example.reoil.view.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.reoil.R
import com.example.reoil.databinding.ActivityLoginBinding
import com.example.reoil.main.MainActivity
import com.example.reoil.view.ViewModelFactory
import com.example.reoil.view.register.RegisterActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory(application)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        binding.loginGoogle.setOnClickListener {
            signIn()
        }

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
            loginViewModel.loginUser(email, password, binding.checkBoxRememberMe.isChecked) { success, message ->
                showLoading(false)
                if (success) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Login failed: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
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

                    showForgotPasswordDialog()
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
                    context = this@LoginActivity,
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
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    loginViewModel.handleSignIn(googleIdTokenCredential) { success, message ->
                        showLoading(false)
                        if (success) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                        }
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

    private fun showForgotPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter your email")

        val view = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)

        builder.setView(view)
        builder.setPositiveButton("Send") { _, _ ->
            val email = emailEditText.text.toString()
            if (email.isNotEmpty()) {
                sendPasswordResetEmail(email)
            } else {
                Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }


    private fun sendPasswordResetEmail(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email has been sent.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to send password reset email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
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
        binding.passwordInputLayout.animate().alpha(1f).setDuration(1000).setStartDelay(1500)
        binding.edtPassword.animate().alpha(1f).setDuration(1000).setStartDelay(1500)
        binding.checkBoxRememberMe.animate().alpha(1f).setDuration(1000).setStartDelay(1700)
        binding.textViewRememberMe.animate().alpha(1f).setDuration(1000).setStartDelay(1900)
        binding.tvForgotpassword.animate().alpha(1f).setDuration(1000).setStartDelay(2100)
        binding.buttonLogin.animate().alpha(1f).setDuration(1000).setStartDelay(2300)
        binding.textViewOr.animate().alpha(1f).setDuration(1000).setStartDelay(2500)
        binding.loginGoogle.animate().alpha(1f).setDuration(1000).setStartDelay(2700)
        binding.logoGoogle.animate().alpha(1f).setDuration(1000).setStartDelay(2900)
        binding.textView3.animate().alpha(1f).setDuration(1000).setStartDelay(3100)
        binding.tvRegister.animate().alpha(1f).setDuration(1000).setStartDelay(3400)
    }
}
