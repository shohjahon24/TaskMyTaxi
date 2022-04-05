package com.example.taskmytaxi.presentation.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.taskmytaxi.R
import com.example.taskmytaxi.databinding.FragmentMapBinding
import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.domain.model.Point
import com.example.taskmytaxi.presentation.session_manager.SessionManager
import com.example.taskmytaxi.util.location.LocationManager
import com.example.taskmytaxi.util.location.isLocationEnabled
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener, View.OnClickListener {

    private var mMap: GoogleMap? = null

    private lateinit var binding: FragmentMapBinding

    private val navController: NavController by lazy { findNavController() }

    private var location: Location? = null

    @Inject
    lateinit var locationManager: LocationManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.clearFocus()
        binding = FragmentMapBinding.bind(view)
        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(this)
        setupObserves()
        setupView()
        binding.btnMyLocation.setOnClickListener(this)
        binding.itemLocation.llFromLocation.setOnClickListener(this)
        binding.itemLocation.llToLocation.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
        binding.btnConfirm.setOnClickListener(this)
    }

    private fun setupView() {
        if (arguments == null) {
            binding.itemLocation.content.visibility = View.VISIBLE
            binding.llFirst.visibility = View.GONE
            SessionManager.locationManager.apply {
                repeat(getToLocations().size) {
                    removeLocation(getToLocations().first())
                }
            }
        } else {
            binding.itemLocation.content.visibility = View.GONE
            binding.llFirst.visibility = View.VISIBLE
            binding.btnBack.visibility = View.VISIBLE
        }
    }

    private fun setupObserves() {
        locationManager.lastDeviceLocation.observe(viewLifecycleOwner) {
            setCamera(it)
        }
        locationManager.selectedLocation.observe(viewLifecycleOwner) {
            location = it
            if (it.address != "") {
                binding.tvLocation.text = it.address
                binding.itemLocation.tvFromLocation.text = it.address
            } else {
                binding.tvLocation.text = it.formattedAddress
                binding.itemLocation.tvFromLocation.text = it.formattedAddress
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        initMap(googleMap)
        SessionManager.locationManager.getFromLocation().let {
            if (it == null)
                locationManager.getCurrentLocation()
            else {
                setCamera(it.point)
            }
        }
    }


    private fun setCamera(point: Point) {
        mMap?.let {
            val p = LatLng(point.lat, point.lng)
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
            isIndoorEnabled = true
            isBuildingsEnabled = true
            uiSettings.isCompassEnabled = false
            uiSettings.isMyLocationButtonEnabled = false
            setOnCameraIdleListener(this@MapFragment)
            context?.let {
                if (!it.isLocationEnabled())
                    setCamera(Point(41.63, 69.24))
                if (
                    ActivityCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
            }
            isMyLocationEnabled = true
        }
    }

    override fun onCameraIdle() {
        mMap?.let {
            val point = Point(it.cameraPosition.target.latitude, it.cameraPosition.target.longitude)
            locationManager.getAddress(point)
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.ll_from_location -> {
                location?.let { SessionManager.locationManager.changeFrom(it) }
                navController.navigate(
                    R.id.action_mapFragment_to_searchFragment,
                    bundleOf(Pair("id", 0), Pair("isEdit", false))
                )
            }
            R.id.ll_to_location -> {
                location?.let { SessionManager.locationManager.changeFrom(it) }
                navController.navigate(
                    R.id.action_mapFragment_to_searchFragment,
                    bundleOf(Pair("id", 1), Pair("isEdit", false))
                )
            }
            R.id.btn_my_location -> {
                locationManager.getCurrentLocation()
            }
            R.id.btn_back -> {
                navController.popBackStack()
            }
            R.id.btn_confirm -> {
                if (navController.currentDestination?.id == R.id.mapFragment) {
                    arguments?.let { it ->
                        when (val p = it.getInt("id")) {
                            0 -> location?.let { SessionManager.locationManager.changeFrom(it) }
                            else -> location?.let {
                                SessionManager.locationManager.changeToLocation(
                                    it,
                                    p
                                )
                            }
                        }
                    }
                    arguments = null
                    navController.navigateUp()
                    navController.navigate(R.id.mainFragment)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.map.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }
}