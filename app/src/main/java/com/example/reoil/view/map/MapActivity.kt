package com.example.reoil.view.map

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.reoil.R
import com.example.reoil.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupView()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Set the map type (e.g. normal, satellite, hybrid)
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Set the camera position (e.g. zoom level, latitude, longitude)
        val cameraPosition = CameraPosition.builder()
            .target(LatLng(-7.983908, 112.621391)) // Malang, Indonesia
            .zoom(12f)
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        // Add a marker (optional)
        val markerOptions = MarkerOptions()
            .position(LatLng(-7.983908, 112.621391))
            .title("Malang")
            .snippet("This is Malang, Indonesia")
        mMap.addMarker(markerOptions)

        // Enable/disable various map features (optional)
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
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
            binding.btWhatsApp.setOnClickListener {
                    val phoneNumber = "+1234567890" // Replace with the phone number you want to use
                    val message = "Hello, this is a test message!" // Replace with the message you want to send

                    val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=$message")
                    val intent = Intent(Intent.ACTION_VIEW, uri)

                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
                    }
                }
            supportActionBar?.hide()
        }
    }