package com.example.taskmytaxi.datasource.network.model

import com.google.gson.annotations.SerializedName

data class RouteDto(
    val distance: Int,
    @SerializedName("eta_minute")
    val duration: Int,
    val points: List<PointDto>,
    @SerializedName("time")
    val durationInSecond: Int
)