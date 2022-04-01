package com.example.taskmytaxi.presentation.ui.map

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.taskmytaxi.R
import com.example.taskmytaxi.databinding.FragmentMapBinding
import com.example.taskmytaxi.domain.model.Point
import com.example.taskmytaxi.util.location.LocationManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener {

    private var mMap: GoogleMap? = null

    private lateinit var binding: FragmentMapBinding

    @Inject
    lateinit var locationManager: LocationManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationManager.getCurrentLocation()
        setupObserves()
        binding.btnMyLocation.setOnClickListener {
            locationManager.getCurrentLocation()
        }
    }

    private fun setupObserves() {
        locationManager.lastDeviceLocation.observe(viewLifecycleOwner) {
            setCamera(it)
        }
        locationManager.selectedLocation.observe(viewLifecycleOwner) {
            if (it.formattedAddress != "") {
                binding.tvLocation.text = it.formattedAddress
                binding.itemLocation.tvFromLocation.text = it.formattedAddress
            } else {
                binding.tvLocation.text = it.address
                binding.itemLocation.tvFromLocation.text = it.address
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        initMap(googleMap)
    }


    private fun setCamera(point: Point) {
        mMap?.let {
            val p = LatLng(point.lat, point.lng)
            /*it.addMarker(
                MarkerOptions().position(p)
                    .icon(
                        BitmapDescriptorFactory.fromBitmap(drawBitmap())
                    ).draggable(true)
            )*/
            it.animateCamera(
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

    private fun initMap(googleMap: GoogleMap?) {
        googleMap?.run {
            mMap = this
            isTrafficEnabled = true
            isMyLocationEnabled = true
            isIndoorEnabled = true
            isBuildingsEnabled = true
            uiSettings.isCompassEnabled = false
            uiSettings.isMyLocationButtonEnabled = false
            setOnCameraIdleListener(this@MapFragment)
        }
    }

    private fun drawBitmap(): Bitmap {
        return BitmapFactory.decodeResource(
            resources,
            R.drawable.ic_pin
        )
    }

    override fun onCameraIdle() {
        mMap?.let {
            val point = Point(it.cameraPosition.target.latitude, it.cameraPosition.target.longitude)
            locationManager.getAddress(point)
        }
    }
}