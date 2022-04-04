package com.example.taskmytaxi.presentation.session_manager

import com.example.taskmytaxi.domain.model.Location

class TempLocationManager {
    private var locations = ArrayList<Location?>()

    fun changeFrom(location: Location) {
        if (locations.isNotEmpty())
            locations[0] = location
        else
            locations.add(location)
    }

    fun addToLocation(location: Location) {
        if (locations.isEmpty())
            locations.add(null)
        locations.add(location)
    }

    fun changeToLocation(location: Location) {
        if (locations.size < 2)
            addToLocation(location)
        else
            locations[1] = location
    }

    fun getFromLocation(): Location? {
        if (locations.isNotEmpty())
            return locations[0]
        return null
    }

    fun getToLocations(): List<Location> {
        if (locations.size <= 1)
            return listOf()

        val result = ArrayList<Location>()
        for (x in 1 until locations.size)
            locations[x]?.let { result.add(it) }
        return result
    }

    fun removeLocation(location: Location) {
        if (locations.indexOf(location) == 0)
            locations[0] = null
        else locations.remove(location)
    }
}