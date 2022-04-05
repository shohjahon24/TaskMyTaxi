package com.example.taskmytaxi.presentation.session_manager

import com.example.taskmytaxi.domain.model.Location

class TempLocationManager {
    private var _locations = ArrayList<Location?>()

    fun changeFrom(location: Location) {
        if (_locations.isNotEmpty())
            _locations[0] = location
        else
            _locations.add(location)
    }

    private fun addToLocation(location: Location) {
        if (_locations.isEmpty())
            _locations.add(null)
        _locations.add(location)
    }

    fun changeToLocation(location: Location, pos: Int) {
        if (_locations.size <= pos)
            addToLocation(location)
        else
            _locations[pos] = location
    }

    fun getFromLocation(): Location? {
        if (_locations.isNotEmpty())
            return _locations[0]
        return null
    }

    fun getToLocations(): List<Location> {
        if (_locations.size <= 1)
            return listOf()

        val result = ArrayList<Location>()
        for (x in 1 until _locations.size)
            _locations[x]?.let { result.add(it) }
        return result
    }

    fun removeLocation(location: Location) {
        if (_locations.indexOf(location) == 0)
            _locations[0] = null
        else _locations.remove(location)
    }
}