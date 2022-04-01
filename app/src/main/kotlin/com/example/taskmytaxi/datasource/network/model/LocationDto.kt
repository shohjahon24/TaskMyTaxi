package com.example.taskmytaxi.datasource.network.model

import com.google.gson.annotations.SerializedName

data class LocationDto(
    val address: String,
    @SerializedName("address_id")
    val addressId: Int?,
    @SerializedName("formatted_address")
    val formattedAddress: String,
    val lang: String,
    val point: PointDto,
    @SerializedName("street_poi_id")
    val streetId: Int
)