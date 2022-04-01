package com.example.taskmytaxi.util.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.util.Log
import androidx.core.location.LocationManagerCompat
import com.example.taskmytaxi.domain.model.Point
import java.io.IOException
import java.util.*


fun Context.isLocationEnabled(): Boolean {
    val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return LocationManagerCompat.isLocationEnabled(locationManager)
}

fun Context.getAddressFromLatLng(point: Point): String {
    var strAdd = ""
    val geoCoder = Geocoder(this, Locale.getDefault())
    try {
        val addresses = geoCoder.getFromLocation(point.lat, point.lng, 10)
        if (addresses != null) {
            Log.d("TAG", "getAddressFromLatLng: $addresses")
            val returnedAddress = addresses[0]
            val strReturnedAddress = StringBuilder("")
            for (i in 0..returnedAddress.maxAddressLineIndex) {
                strReturnedAddress.append(returnedAddress.subAdminArea)
            }
            strAdd = strReturnedAddress.toString()
            // Log.w("My Current location address", strReturnedAddress.toString())
        } else {
            // Log.w("My Current location address", "No Address returned!")
        }
    } catch (e: Exception) {
        // e.printStackTrace()
        // Log.w("My Current location address", "Can't get Address!")
    }
    /*val location1 = if (strAdd != "") this.getLocationFromAddress(strAdd) else null
    if (location1 != null) {
        val distance = distanceBetween(Location(location.longitude, location.latitude), location1)
        if (distance != 0f)
            strAdd = "${(((distance / 1000 * 0.621371) * 1000).toInt().toFloat() / 1000)} mile from $strAdd"
    }*/
    return strAdd
}

fun Context.getAddress(point: Point): Address? {
    val geoCoder = Geocoder(this, Locale.getDefault())
    return try {
        val addresses = geoCoder.getFromLocation(point.lat, point.lng, 1)
        addresses[0]
    } catch (e: Exception) {
        null
    }

}

fun Context.getLocationFromAddress(strAddress: String?): Point? {
    val coder = Geocoder(this)
    val address: List<Address>?
    var p1: Point? = null
    try {
        // May throw an IOException
        address = coder.getFromLocationName(strAddress, 5)
        if (address == null || address.isEmpty()) {
            return null
        }
        val location: Address = address[0]
        p1 = Point(location.latitude, location.longitude)
    } catch (ex: IOException) {
        // ex.printStackTrace()
    }
    return p1
}
