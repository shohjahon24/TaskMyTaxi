package com.example.taskmytaxi.presentation.list

import com.example.taskmytaxi.domain.model.Location

interface LocationListener {
    fun onItemClick(location: Location)
}