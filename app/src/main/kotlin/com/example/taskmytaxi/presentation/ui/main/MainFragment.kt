package com.example.taskmytaxi.presentation.ui.main

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.taskmytaxi.R
import com.example.taskmytaxi.databinding.DialogStopPointsBinding
import com.example.taskmytaxi.databinding.FragmentMainBinding
import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.domain.model.Point
import com.example.taskmytaxi.presentation.list.StopPointListener
import com.example.taskmytaxi.presentation.list.stop_point.StopPointAdapter
import com.example.taskmytaxi.presentation.session_manager.SessionManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main), OnMapReadyCallback, View.OnClickListener,
    StopPointListener {

    private var mMap: GoogleMap? = null
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var toLocations: List<Location?> = arrayListOf()
    private var fromLocation: Location? = null
    private var stopPointDialog: BottomSheetDialog? = null
    private val markers: ArrayList<Marker?> = ArrayList()
    private val polyLines: ArrayList<Polyline> = ArrayList()

    @Inject
    lateinit var stopPointAdapter: StopPointAdapter

    private val navController: NavController by lazy { findNavController() }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onKeyListener(view)
        binding = FragmentMainBinding.bind(view)
        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(this)
        SessionManager.locationManager.getFromLocation()?.let { location ->
            fromLocation = location
        }
        fromLocation?.let { binding.tvFromLocation.text = it.address }
        toLocations = SessionManager.locationManager.getToLocations()
        if (toLocations.isNotEmpty()) {
            if (toLocations.size == 1)
                binding.tvToLocation.text = toLocations.first()?.address
            else
                binding.tvToLocation.text = "${toLocations.size} addresses"
        }
        setupObserves()

        binding.btnAdd.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
        binding.btnOrder.setOnClickListener(this)
        binding.tvToLocation.setOnClickListener(this)
        binding.tvFromLocation.setOnClickListener(this)
        stopPointAdapter.listener = this
    }

    private fun setupObserves() {
        viewModel.apply {
            points.observe(viewLifecycleOwner) {
                setCamera(it[it.size / 2])
                drawRoute(it)
            }
        }
    }

    private fun drawPoints() {
        markers.forEach {
            it?.remove()
        }
        markers.clear()
        fromLocation?.point?.let { addMarker(it, R.drawable.ic_point_red) }
        toLocations.forEach {
            it?.let { it1 -> addMarker(it1.point, R.drawable.ic_point_blue) }
        }
    }

    private fun addMarker(point: Point, resourceID: Int) {
        mMap?.apply {
            val p = LatLng(point.lat, point.lng)
            val m = addMarker(
                MarkerOptions().position(p)
                    .icon(
                        drawBitmap(resourceID)
                    ).draggable(true).anchor(.4f, .7f)
            )
            markers.add(m)
        }
    }

    private fun drawRoute(points: List<LatLng>) {
        mMap?.let { it ->
            val p = it.addPolyline(PolylineOptions().addAll(points))
            p.color = ContextCompat.getColor(requireContext(), R.color.blue)
            polyLines.add(p)
        }
    }

    private fun drawBitmap(resourceID: Int): BitmapDescriptor? {
        return context?.let {
            ContextCompat.getDrawable(it, resourceID)?.run {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                val bitmap =
                    Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
                draw(Canvas(bitmap))
                BitmapDescriptorFactory.fromBitmap(bitmap)
            }
        }
    }

    private fun setCamera(point: LatLng) {
        mMap?.let {
            it.animateCamera(
                CameraUpdateFactory.newLatLngZoom(point, 12f),
                200,
                object : GoogleMap.CancelableCallback {
                    override fun onCancel() {

                    }

                    override fun onFinish() {
                    }
                })
        }
    }

    private fun showDropPointDialog() {
        val binding = DialogStopPointsBinding.inflate(layoutInflater)
        if (toLocations.size > 3)
            binding.tvAddStopPoint.visibility = View.GONE
        else
            binding.tvAddStopPoint.visibility = View.VISIBLE
        if (stopPointDialog == null) {
            context?.let { it ->
                binding.apply {
                    listPoint.adapter = stopPointAdapter
                    tvAddStopPoint.setOnClickListener(this@MainFragment)
                }
                toLocations.forEach {
                    it?.let {
                        stopPointAdapter.addData(it)
                    }
                }
                stopPointDialog = BottomSheetDialog(it, R.style.BottomSheetDialogTheme)
                stopPointDialog?.run {
                    setContentView(binding.root)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
        stopPointDialog?.show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (fromLocation == null)
            toLocations[0]?.let {
                setCamera(LatLng(it.point.lat, it.point.lng))
            }
        else
            fromLocation?.let {
                if (toLocations.isNotEmpty()) {
                    viewModel.drawLine(it, toLocations)
                }
                drawPoints()
                setCamera(LatLng(it.point.lat, it.point.lng))
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

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btn_back -> navController.popBackStack()
            R.id.btn_add -> {
                if (toLocations.isEmpty())
                    navigateToSearch(1)
                else
                    showDropPointDialog()
            }
            R.id.tv_from_location -> {
                navigateToSearch(0)
            }
            R.id.tv_to_location -> {
                if (toLocations.size < 2)
                    navigateToSearch(1)
                else {
                    showDropPointDialog()
                }
            }
            R.id.tv_add_stop_point -> {
                stopPointDialog?.dismiss()
                navigateToSearch(toLocations.size + 1)
            }
        }
    }

    override fun onEditClick(pos: Int) {
        stopPointDialog?.dismiss()
        navigateToSearch(pos + 1)
    }

    @SuppressLint("SetTextI18n")
    override fun onRemoveClick() {
        toLocations = SessionManager.locationManager.getToLocations()
        if (toLocations.isNotEmpty()) {
            fromLocation?.let { viewModel.drawLine(it, toLocations) }
            if (toLocations.size == 1)
                binding.tvToLocation.text = toLocations.first()?.address
            else
                binding.tvToLocation.text = "${toLocations.size} addresses"
        } else {
            binding.tvToLocation.text = "Where to?"
            stopPointDialog?.dismiss()
        }
        polyLines.forEach {
            it.remove()
        }
        drawPoints()
    }

    private fun navigateToSearch(id: Int) {
        navController.navigate(
            R.id.action_mainFragment_to_searchFragment,
            bundleOf(Pair("id", id), Pair("isEdit", true))
        )
    }

    private fun onKeyListener(view: View) {
        view.clearFocus()
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                navController.navigateUp()
                navController.navigate(R.id.map)
                true
            } else false
        }
    }
}