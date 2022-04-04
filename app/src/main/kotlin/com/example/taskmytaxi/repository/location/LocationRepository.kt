package com.example.taskmytaxi.repository.location

import com.example.taskmytaxi.datasource.network.ClientResponse
import com.example.taskmytaxi.domain.model.Locations
import com.example.taskmytaxi.domain.model.Point
import com.example.taskmytaxi.domain.model.Route

interface LocationRepository {

    suspend fun search(query: String): ClientResponse<List<Locations>>

    suspend fun getRoute(pointFrom: Point, pointTo: Point): ClientResponse<Route>

}
