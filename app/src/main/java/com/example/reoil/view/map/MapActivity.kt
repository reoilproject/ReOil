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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            onBackPressed()
        }


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

        // Add three markers
        val marker1 = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(-7.983908, 112.621391))
                .title("Warung Pamungkas")
        )
        val marker2 = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(-7.983500, 112.623000))
                .title("Warung Adam")
        )
        val marker3 = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(-7.984000, 112.624000))
                .title("Warung Amru")
        )

        // Enable/disable various map features (optional)
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        binding.tvPengepul.text = marker.title
        return true
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

            if (intent.resolveActivity(packageManager)!= null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
            }
        }
        supportActionBar?.hide()
    }
}