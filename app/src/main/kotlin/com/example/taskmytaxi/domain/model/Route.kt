package com.example.taskmytaxi.domain.model

data class Route(
    val distance: Int,
    val duration: Int,
    val durationInSecond: Int,
    val points: List<Point>
)