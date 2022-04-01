package com.example.taskmytaxi.util.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.example.taskmytaxi.domain.model.Point
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("MissingPermission")
@Singleton
class LocationManager @Inject constructor(@ApplicationContext private val context: Context) {

    var lastDeviceLocation: MutableLiveData<Point> = MutableLiveData()

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            context
        )
    }

    private val locationRequest by lazy {
        LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            val location = p0.lastLocation
            lastDeviceLocation.postValue(Point(location.latitude, location.longitude))
        }
    }

    fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun getCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: android.location.Location? ->
                location?.let {
                    lastDeviceLocation.postValue(Point(location.latitude, location.longitude))
                }
            }
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}