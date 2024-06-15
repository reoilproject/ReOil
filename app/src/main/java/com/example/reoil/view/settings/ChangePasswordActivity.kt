package com.example.reoil.view.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.reoil.databinding.ActivityChangePasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Setup button listeners
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnGallery.setOnClickListener {
            val oldPassword = binding.oldpassword.text.toString().trim()
            val newPassword = binding.newPassword.text.toString().trim()
            val confirmNewPassword = binding.ConfirmNewPassword.text.toString().trim()

            if (validateForm(oldPassword, newPassword, confirmNewPassword)) {
                changePassword(oldPassword, newPassword)
            }
        }
    }

    private fun validateForm(oldPassword: String, newPassword: String, confirmNewPassword: String): Boolean {
        var valid = true

        if (oldPassword.isEmpty()) {
            binding.oldpassword.error = "Required."
            valid = false
        }

        if (newPassword.isEmpty()) {
            binding.newPassword.error = "Required."
            valid = false
        } else if (newPassword != confirmNewPassword) {
            binding.ConfirmNewPassword.error = "Passwords do not match."
            valid = false
        }

        return valid
    }

    private fun changePassword(oldPassword: String, newPassword: String) {
        val user = auth.currentUser
        val credential = EmailAuthProvider.getCredential(user!!.email!!, oldPassword)

        user.reauthenticate(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                user.updatePassword(newPassword).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Re-authentication failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

