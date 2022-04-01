package com.example.taskmytaxi.presentation.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.taskmytaxi.R
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainFragment : Fragment(R.layout.fragment_main), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        // Add a marker in Sydney and move the camera
        val p = LatLng(41.311081, 69.240562)
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(p, 15f),
            200,
            object : GoogleMap.CancelableCallback {
                override fun onCancel() {

                }

                override fun onFinish() {
                }
            })
    }
}