package com.example.taskmytaxi.datasource.network.request

data class SearchRequest(
    val limit: Int,
    val lat: Double,
    val lng: Double
)