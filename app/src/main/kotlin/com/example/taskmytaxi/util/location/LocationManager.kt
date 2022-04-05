package com.example.taskmytaxi.util.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.lifecycle.MutableLiveData
import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.domain.model.Point
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationManager @Inject constructor(@ApplicationContext private val context: Context) {

    var lastDeviceLocation: MutableLiveData<Point> = MutableLiveData()

    var selectedLocation: MutableLiveData<Location> = MutableLiveData()

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

 /*   fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }*/

    fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            lastDeviceLocation.value = Point(41.63, 69.24)
             return
        }
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

    fun getAddress(point: Point) {
        CoroutineScope(Dispatchers.IO).launch {
            var connection: HttpURLConnection? = null
            val location: Location
            val jsonResult = StringBuilder()
            try {
                val sb = StringBuilder("https://nominatim.openstreetmap.org/reverse?format=jsonv2&")
                sb.append("lat=${point.lat}")
                sb.append("&lon=${point.lng}")
                sb.append("&zoom=18&addressdetails=0")
                val url = URL(sb.toString())
                connection = url.openConnection() as HttpURLConnection
                val inputStreamReader = InputStreamReader(connection.inputStream)
                var read: Int
                val buff = CharArray(1024)
                while (inputStreamReader.read(buff).also { read = it } != -1) {
                    jsonResult.append(buff, 0, read)
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
            try {
                val jsonObject = JSONObject(jsonResult.toString())
                val name = jsonObject.getString("display_name")
                var address = ""
                var formattedAddress = ""
                val list = name.split(",")
                list.forEachIndexed { index, s ->
                    when {
                        index < list.size - 5 -> address += "$s,"
                        index < list.size - 4 -> address += s
                        index == list.size - 1 -> formattedAddress += s
                        else -> formattedAddress += "$s,"
                    }
                }
                location = Location(address, 0, formattedAddress, "", point, 0)
                selectedLocation.postValue(location)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}