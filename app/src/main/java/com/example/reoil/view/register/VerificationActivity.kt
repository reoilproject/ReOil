package com.example.reoil.view.register

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.reoil.R
import com.example.reoil.databinding.ActivityVerificationBinding
import com.google.firebase.auth.FirebaseAuth

class VerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationBinding
    private lateinit var auth: FirebaseAuth
    private var user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        auth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(this, getString(R.string.user_not_logged_in), Toast.LENGTH_SHORT).show()
            return
        }

        binding.emailsender.text = user?.email

        binding.buttonVerif.setOnClickListener {
            user?.reload()?.addOnCompleteListener {
                if (user?.isEmailVerified == true) {
                    val intent = Intent(this, FormProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.email_not_verified),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.tvDidntReceive.setOnClickListener {
            user?.sendEmailVerification()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        getString(R.string.verification_email_sent),
                        Toast.LENGTH_SHORT
                    ).show()
                    startResendCountdown()
                } else {
                    val errorMessage = task.exception?.message ?: ""
                    Toast.makeText(
                        this,
                        getString(R.string.verification_email_failed, errorMessage),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        startResendCountdown()
    }

    private fun startResendCountdown() {
        binding.tvDidntReceive.isEnabled = false
        object : CountDownTimer(60000, 1000) { // 60 detik

            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.tvDidntReceive.text = getString(R.string.wait_seconds, secondsRemaining)
            }

            override fun onFinish() {
                binding.tvDidntReceive.isEnabled = true
                binding.tvDidntReceive.text = getString(R.string.didnt_receive)
            }
        }.start()
    }
}
