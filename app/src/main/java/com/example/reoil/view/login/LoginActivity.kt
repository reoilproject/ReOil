package com.example.reoil.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.reoil.main.MainActivity
import com.example.reoil.R
import com.example.reoil.databinding.ActivityLoginBinding
import com.example.reoil.utils.PreferencesHelper
import com.example.reoil.view.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        preferencesHelper = PreferencesHelper(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.loginGoogle.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener {
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

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

                    Toast.makeText(this@LoginActivity, "Coming soon!", Toast.LENGTH_SHORT).show()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
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
                    val intent = Intent(this, MainActivity::class.java)
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

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    if (binding.checkBoxRememberMe.isChecked) {
                        preferencesHelper.setLoginStatus(true)
                    } else {
                        preferencesHelper.clearLoginStatus()
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            showLoading(true)
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                showLoading(false)
                Toast.makeText(this, "Google sign in failed: ${e.statusCode}", Toast.LENGTH_LONG)
                    .show()
            }
        }
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
        binding.textViewOr.animate().alpha(1f).setDuration(1000).setStartDelay(2500)
        binding.loginGoogle.animate().alpha(1f).setDuration(1000).setStartDelay(2700)
        binding.logoGoogle.animate().alpha(1f).setDuration(1000).setStartDelay(2900)
        binding.iconReoil.animate().alpha(1f).setDuration(1000).setStartDelay(3100)
        binding.textView3.animate().alpha(1f).setDuration(1000).setStartDelay(3300)
        binding.tvRegister.animate().alpha(1f).setDuration(1000).setStartDelay(3500)
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
