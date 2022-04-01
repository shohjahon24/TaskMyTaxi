package com.example.taskmytaxi.domain.model

data class Location(
    val address: String,
    val addressId: Int?,
    val formattedAddress: String,
    val lang: String,
    val point: Point,
    val streetId: Int
)