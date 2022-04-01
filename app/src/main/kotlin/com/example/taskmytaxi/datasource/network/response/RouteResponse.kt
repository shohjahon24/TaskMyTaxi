package com.example.taskmytaxi.datasource.network.response

import com.example.taskmytaxi.datasource.network.model.RouteDto
import com.google.gson.annotations.SerializedName

data class RouteResponse(
    val data: RouteData,
    val status: String
)

data class RouteData(
    @SerializedName("object")
    val route: RouteDto
)