package com.example.taskmytaxi.presentation.ui.map

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.taskmytaxi.R
import com.example.taskmytaxi.domain.model.Point
import com.example.taskmytaxi.util.location.LocationManager
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private var mMap: GoogleMap? = null

    @Inject
    lateinit var locationManager: LocationManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationManager.getCurrentLocation()
        setupObserves()
    }

    private fun setupObserves() {
        locationManager.lastDeviceLocation.observe(viewLifecycleOwner) {
            setMarker(it)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.run {
            isTrafficEnabled = true
            isIndoorEnabled = true
            isBuildingsEnabled = true
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isCompassEnabled = true
            uiSettings.isIndoorLevelPickerEnabled = true
            uiSettings.isZoomGesturesEnabled = true
            uiSettings.isTiltGesturesEnabled = true
            uiSettings.setAllGesturesEnabled(true)
            mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }

    private fun setMapStyle() {
        mMap?.let {
            try {
                val success = it.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireActivity(),
                        R.raw.map_style
                    )
                )

                if (!success) {

                }
            } catch (e: Resources.NotFoundException) {

            }
        }
    }


    private fun setMarker(point: Point) {
        mMap?.let {
            val p = LatLng(point.lat, point.lng)
            it.addMarker(
                MarkerOptions().position(p)
                    .icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.pin)
                    )
                    .draggable(true)
            )
            it.animateCamera(
                CameraUpdateFactory.newLatLngZoom(p, 15f),
                2000,
                object : GoogleMap.CancelableCallback {
                    override fun onCancel() {

                    }

                    override fun onFinish() {
                    }

                })
        }
    }
}