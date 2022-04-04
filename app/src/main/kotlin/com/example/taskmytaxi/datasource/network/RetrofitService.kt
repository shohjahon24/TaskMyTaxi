package com.example.taskmytaxi.datasource.network

import com.example.taskmytaxi.datasource.network.response.RouteResponse
import com.example.taskmytaxi.datasource.network.response.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface LocationService {

    @GET("v2/search")
    suspend fun search(
        @Query("limit") limit: Int,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("query") query: String,
        @Query("street_poi_id") streetId: Int
    ): Response<SearchResponse>

    @GET("v1/route/from/{from}/to/{to}")
    suspend fun getRoute(
        @Path("from") from: String,
        @Path("to") to: String
    ): Response<RouteResponse>
}











