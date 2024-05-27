package com.example.reoil.login

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.reoil.MainActivity
import com.example.reoil.R
import com.example.reoil.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var textView: TextView
    private lateinit var textView2: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var textViewPassword: TextView
    private lateinit var edt_email: EditText
    private lateinit var edt_password: EditText
    private lateinit var buttonLogin: Button
    private lateinit var checkBoxRememberMe: CheckBox
    private lateinit var textViewRememberMe: TextView
    private lateinit var tv_forgotpassword: TextView
    private lateinit var textView3: TextView
    private lateinit var tv_register: TextView
    private lateinit var textViewOr: TextView
    private lateinit var loginGoogle: Button
    private lateinit var logoGoogle: ImageView
    private lateinit var iconReoil: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Inisialisasi komponen UI
        textView = findViewById(R.id.textView)
        textView2 = findViewById(R.id.textView2)
        textView3 = findViewById(R.id.textView3)
        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe)
        textViewEmail = findViewById(R.id.textViewEmail)
        textViewPassword = findViewById(R.id.textViewPassword)
        edt_email = findViewById(R.id.edt_email)
        edt_password = findViewById(R.id.edt_password)
        buttonLogin = findViewById(R.id.buttonLogin)
        tv_register = findViewById(R.id.tv_register)
        tv_forgotpassword = findViewById(R.id.tv_forgotpassword)
        textViewRememberMe = findViewById(R.id.textViewRememberMe)
        textViewOr = findViewById(R.id.textViewOr)
        loginGoogle = findViewById(R.id.loginGoogle)
        logoGoogle = findViewById(R.id.logoGoogle)
        iconReoil = findViewById(R.id.iconReoil)

        // Konfigurasi Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        loginGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Tambahkan animasi ke text views
        animateTextViews()

        buttonLogin.setOnClickListener {
            val email = edt_email.text.toString()
            val password = edt_password.text.toString()

            if (email.isEmpty()) {
                edt_email.error = "Email is required"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                edt_password.error = "Password is required"
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        tv_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        tv_forgotpassword.setOnClickListener {
            // Buat objek ScaleAnimation
            val animation = ScaleAnimation(
                1f, 1.2f, // Animasi skala horizontal dari 1 ke 1.2
                1f, 1.2f, // Animasi skala vertikal dari 1 ke 1.2
                Animation.RELATIVE_TO_SELF, 0.5f, // Posisi awal horizontal tengah
                Animation.RELATIVE_TO_SELF, 0.5f // Posisi awal vertikal tengah
            )

            // Atur durasi animasi
            animation.duration = 150 // Durasi 150 millisecond

            // Atur fillAfter supaya posisi setelah animasi tetap
            animation.fillAfter = true

            // Mulai animasi
            tv_forgotpassword.startAnimation(animation)

            // Tampilkan pesan "Coming soon!" setelah animasi selesai
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // Kosong
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // Buat objek ScaleAnimation
                    val reverseAnimation = ScaleAnimation(
                        1.2f, 1.0f, // Animasi skala horizontal dari 1.2 ke 1
                        1.2f, 1.0f, // Animasi skala vertikal dari 1.2 ke 1
                        Animation.RELATIVE_TO_SELF, 0.5f, // Posisi awal horizontal tengah
                        Animation.RELATIVE_TO_SELF, 0.5f // Posisi awal vertikal tengah
                    )

                    // Atur durasi animasi
                    reverseAnimation.duration = 150 // Durasi 150 millisecond

                    // Atur fillAfter supaya posisi setelah animasi tetap
                    reverseAnimation.fillAfter = false

                    // Mulai animasi
                    tv_forgotpassword.startAnimation(reverseAnimation)

                    // Tampilkan pesan "Coming soon!" setelah animasi selesai
                    Toast.makeText(this@LoginActivity, "Coming soon!", Toast.LENGTH_SHORT).show()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // Kosong
                }
            })
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login sukses, arahkan ke MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Jika login gagal, tampilkan pesan ke pengguna
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun animateTextViews() {
        textView.animate().alpha(1f).setDuration(1000).setStartDelay(500)
        textView2.animate().alpha(1f).setDuration(1000).setStartDelay(700)
        textViewEmail.animate().alpha(1f).setDuration(1000).setStartDelay(900)
        edt_email.animate().alpha(1f).setDuration(1000).setStartDelay(1100)
        textViewPassword.animate().alpha(1f).setDuration(1000).setStartDelay(1300)
        edt_password.animate().alpha(1f).setDuration(1000).setStartDelay(1500)
        checkBoxRememberMe.animate().alpha(1f).setDuration(1000).setStartDelay(1700)
        textViewRememberMe.animate().alpha(1f).setDuration(1000).setStartDelay(1900)
        tv_forgotpassword.animate().alpha(1f).setDuration(1000).setStartDelay(2100)
        buttonLogin.animate().alpha(1f).setDuration(1000).setStartDelay(2300)
        textViewOr.animate().alpha(1f).setDuration(1000).setStartDelay(2500)
        loginGoogle.animate().alpha(1f).setDuration(1000).setStartDelay(2700)
        logoGoogle.animate().alpha(1f).setDuration(1000).setStartDelay(2900)
        iconReoil.animate().alpha(1f).setDuration(1000).setStartDelay(3100)
        textView3.animate().alpha(1f).setDuration(1000).setStartDelay(3300)
        tv_register.animate().alpha(1f).setDuration(1000).setStartDelay(3500)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Hasil dari Intent Google Sign-In
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.statusCode}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
