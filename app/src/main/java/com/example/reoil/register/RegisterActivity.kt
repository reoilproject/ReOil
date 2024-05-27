package com.example.reoil.register

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.reoil.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi FirebaseAuth
        auth = FirebaseAuth.getInstance()

        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edtConfirmpassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        setupView()
        setupAction()
        animateTextViews()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmpassword.text.toString()

            if (email.isEmpty()) {
                binding.edtEmail.error = "Email is required"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.edtPassword.error = "Password is required"
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.edtConfirmpassword.error = "Passwords do not match"
                return@setOnClickListener
            }

            registerUser(email, password)
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registrasi sukses, tampilkan dialog konfirmasi
                    AlertDialog.Builder(this).apply {
                        setTitle("Yeah!")
                        setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
                        setPositiveButton("Lanjut") { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                } else {
                    // Jika registrasi gagal, tampilkan pesan ke pengguna
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun animateTextViews() {
        binding.titleTextView.animate().alpha(1f).setDuration(1000).setStartDelay(500)
        binding.detailRegisterText.animate().alpha(1f).setDuration(1000).setStartDelay(700)
        binding.nameTextView.animate().alpha(1f).setDuration(1000).setStartDelay(900)
        binding.usernameEditText.animate().alpha(1f).setDuration(1000).setStartDelay(1100)
        binding.textViewEmail.animate().alpha(1f).setDuration(1000).setStartDelay(1300)
        binding.edtEmail.animate().alpha(1f).setDuration(1000).setStartDelay(1500)
        binding.textViewPassword.animate().alpha(1f).setDuration(1000).setStartDelay(1700)
        binding.edtPassword.animate().alpha(1f).setDuration(1000).setStartDelay(1900)
        binding.ConfirmpasswordTextView.animate().alpha(1f).setDuration(1000).setStartDelay(2100)
        binding.edtConfirmpassword.animate().alpha(1f).setDuration(1000).setStartDelay(2300)
        binding.signupButton.animate().alpha(1f).setDuration(1000).setStartDelay(2500)
        binding.iconreoil.animate().alpha(1f).setDuration(1000).setStartDelay(2700)
        binding.textView3.animate().alpha(1f).setDuration(1000).setStartDelay(2900)
        binding.tvLogin.animate().alpha(1f).setDuration(1000).setStartDelay(3100)
    }
}
