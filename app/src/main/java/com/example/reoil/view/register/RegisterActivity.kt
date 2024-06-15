package com.example.reoil.view.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.reoil.R
import com.example.reoil.databinding.ActivityRegisterBinding
import com.example.reoil.view.ViewModelFactory
import com.example.reoil.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels { ViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        observeViewModel()
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

            showLoading(true)
            registerViewModel.registerUser(email, password)
        }
    }

    private fun observeViewModel() {
        registerViewModel.registrationStatus.observe(this, Observer { status ->
            showLoading(false)
            if (status) {
                val email = binding.edtEmail.text.toString()
                val intent = Intent(this, VerificationActivity::class.java).apply {
                    putExtra("email", email)
                }
                startActivity(intent)
                finish()
                AlertDialog.Builder(this).apply {
                    setTitle("Yeah!")
                    setMessage("Akun dengan $email sudah jadi nih. Kami telah mengirim email verifikasi. Silakan cek email Anda.")
                    setPositiveButton("Lanjut") { _, _ -> }
                    create()
                    show()
                }
            }
        })

        registerViewModel.verificationEmailStatus.observe(this, Observer { status ->
            if (!status) {
                showLoading(false)
                Toast.makeText(this, "Gagal mengirim email verifikasi: ${registerViewModel.errorMessage.value}", Toast.LENGTH_SHORT).show()
            }
        })

        registerViewModel.errorMessage.observe(this, Observer { message ->
            if (message != null) {
                showLoading(false)
                Toast.makeText(this, "Pendaftaran gagal: $message", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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
