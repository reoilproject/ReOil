package com.example.reoil.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.reoil.R
import com.example.reoil.databinding.ActivityRegisterBinding
import com.example.reoil.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        setupView()
        setupAction()
        animateTextViews()
    }

    private fun setupView() {
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
                binding.edtEmail.error = getString(R.string.error_email_required)
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.edtPassword.error = getString(R.string.error_password_required)
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.edtConfirmpassword.error = getString(R.string.error_password_mismatch)
                return@setOnClickListener
            }

            registerUser(email, password)
        }
    }


    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    AlertDialog.Builder(this).apply {
                        setTitle("Yeah!")
                        setMessage("Akun dengan $email sudah jadi nih. Yuk, ubah minyak kotormu jadi uang.")
                        setPositiveButton("Lanjut") { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun animateTextViews() {
        binding.titleTextView.animate().alpha(1f).setDuration(1000).setStartDelay(500)
        binding.detailRegisterText.animate().alpha(1f).setDuration(1000).setStartDelay(700)
        binding.textViewEmail.animate().alpha(1f).setDuration(1000).setStartDelay(900)
        binding.edtEmail.animate().alpha(1f).setDuration(1000).setStartDelay(1300)
        binding.textViewPassword.animate().alpha(1f).setDuration(1000).setStartDelay(1500)
        binding.edtPassword.animate().alpha(1f).setDuration(1000).setStartDelay(1700)
        binding.ConfirmpasswordTextView.animate().alpha(1f).setDuration(1000).setStartDelay(1900)
        binding.edtConfirmpassword.animate().alpha(1f).setDuration(1000).setStartDelay(2100)
        binding.signupButton.animate().alpha(1f).setDuration(1000).setStartDelay(2300)
        binding.iconreoil.animate().alpha(1f).setDuration(1000).setStartDelay(2500)
        binding.textView3.animate().alpha(1f).setDuration(1000).setStartDelay(2700)
        binding.tvLogin.animate().alpha(1f).setDuration(1000).setStartDelay(2900)
    }
}
