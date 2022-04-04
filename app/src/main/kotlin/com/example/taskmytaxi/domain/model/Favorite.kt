package com.example.taskmytaxi.domain.model

data class Favorite(
    val address: String,
    val formattedAddress: String,
    val description: String,
    val point: Point,
    val type: Int
)