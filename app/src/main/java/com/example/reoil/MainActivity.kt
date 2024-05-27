package com.example.reoil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.reoil.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textViewName: TextView
    private lateinit var buttonLogout: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Dapatkan data pengguna yang sedang login
        val user = auth.currentUser

        // Referensi ke komponen UI
        imageView = findViewById(R.id.iv_image)
        textViewName = findViewById(R.id.tv_name)
        buttonLogout = findViewById(R.id.bt_logout)

        // Update UI dengan informasi pengguna
        updateUI(user)

        // Logika untuk logout
        buttonLogout.setOnClickListener {
            // Logout dari Firebase
            FirebaseAuth.getInstance().signOut()

            // Arahkan kembali ke LoginActivity dan tutup MainActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Set nama pengguna ke TextView
            textViewName.text = user.displayName

            // Jika URL foto profil tidak null, muat dengan Glide
            user.photoUrl?.let { imageUrl ->
                Glide.with(this).load(imageUrl).into(imageView)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Cek jika pengguna tidak null
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Pengguna belum login, arahkan ke LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}