package com.example.reoil.view.map

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.reoil.R
import com.example.reoil.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding
    private var selectedMarker: Marker? = null

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
                .icon(resizeIcon(R.drawable.logo_reoil, 100, 100))
        )
        if (marker1 != null) {
            marker1.tag = "+6281234567890"
        }

        val marker2 = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(-7.983500, 112.623000))
                .title("Warung Adam")
                .icon(resizeIcon(R.drawable.logo_reoil, 100, 100))
        )
        if (marker2 != null) {
            marker2.tag = "+6281381641530"
        }

        val marker3 = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(-7.984000, 112.624000))
                .title("Warung Amru")
                .icon(resizeIcon(R.drawable.logo_reoil, 100, 100))
        )

        val marker4 = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(-7.981000, 112.626000))
                .title("Warung April")
                .icon(resizeIcon(R.drawable.logo_reoil, 100, 100))
        )
        val marker5 = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(-7.986000, 112.620000))
                .title("Warung Amel")
                .icon(resizeIcon(R.drawable.logo_reoil, 100, 100))
        )
        if (marker5 != null) {
            marker5.tag = "+6281338491334"
        }
        val marker6 = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(-7.980000, 112.618000))
                .title("Warung Naswa")
                .icon(resizeIcon(R.drawable.logo_reoil, 100, 100))
        )
        val marker7 = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(-7.987000, 112.622000))
                .title("Warung Rafli")
                .icon(resizeIcon(R.drawable.logo_reoil, 100, 100))
        )

        // Enable/disable various map features (optional)
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        mMap.setOnMarkerClickListener(this)
        getMyLocation()
        setMapStyle()
    }

    fun resizeIcon(iconId: Int, width: Int, height: Int): BitmapDescriptor {
        val bitmap = BitmapFactory.decodeResource(resources, iconId)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        selectedMarker = marker
        binding.tvPengepul.text = marker.title
        return true
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun setupView() {
        binding.btWhatsApp.setOnClickListener {
            if (selectedMarker!= null) {
                val whatsappNumber = selectedMarker!!.tag as String
                val whatsappMessage = "Halo, saya ingin menukar minyak jelantah saya ke toko ${selectedMarker!!.title}"
                val whatsappUrl = "https://wa.me/$whatsappNumber?text=$whatsappMessage"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(whatsappUrl)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Select a marker first", Toast.LENGTH_SHORT).show()
            }
        }
        supportActionBar?.hide()
    }
    companion object {
        private const val TAG = "MapActivity"
    }
}