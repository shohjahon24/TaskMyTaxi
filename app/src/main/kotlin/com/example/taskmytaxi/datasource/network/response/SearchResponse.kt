package com.example.taskmytaxi.datasource.network.response

import com.example.taskmytaxi.datasource.network.model.LocationDto
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val data: Candidate,
    val status: String
)

data class Candidate(
    @SerializedName("candidates")
    val locations: List<LocationDto>
)
