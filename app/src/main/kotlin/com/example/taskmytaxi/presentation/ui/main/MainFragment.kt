package com.example.taskmytaxi.presentation.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.taskmytaxi.R
import com.example.taskmytaxi.databinding.FragmentMainBinding
import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.domain.model.Point
import com.example.taskmytaxi.presentation.session_manager.SessionManager
import com.example.taskmytaxi.presentation.ui.search.SearchViewModel
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var toLocations: List<Location?> = arrayListOf()
    private var fromLocation: Location? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMainBinding.bind(view)
        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(this)
        fromLocation = SessionManager.locationManager.getFromLocation()
        toLocations = SessionManager.locationManager.getToLocations()
        if (toLocations.isNotEmpty())
            binding.tvToLocation.text = toLocations.first()?.address
        fromLocation?.let { binding.tvFromLocation.text = it.address }
        setupObserves()
    }

    private fun setupObserves() {
        viewModel.apply {
            points.observe(viewLifecycleOwner) {
                Toast.makeText(context, "${it.size}", Toast.LENGTH_SHORT).show()
                drawRoute(it)
            }
        }
    }

    private fun drawRoute(points: List<LatLng>) {
        mMap?.let {
            it.addPolyline(PolylineOptions().addAll(points))
        }
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


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        fromLocation?.let {
            if (toLocations.isNotEmpty()) {
                viewModel.getRoute(it.point, toLocations.first()!!.point)
            }
            setCamera(it.point)
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