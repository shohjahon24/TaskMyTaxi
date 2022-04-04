package com.example.taskmytaxi.presentation.list

import com.example.taskmytaxi.domain.model.Location

interface OnLocationClick {
    fun onItemClick(location: Location)
}