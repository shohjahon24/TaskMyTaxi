package com.example.taskmytaxi.datasource.network

import com.example.taskmytaxi.datasource.network.request.RouteRequest
import com.example.taskmytaxi.datasource.network.request.SearchRequest
import com.example.taskmytaxi.datasource.network.response.RouteResponse
import com.example.taskmytaxi.datasource.network.response.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Path


interface LocationService {

    @GET("v2/search")
    suspend fun search(
        @Part("limit") limit: Int,
        @Part("lat") lat: Double,
        @Part("lng") lng: Double,
        @Part("query") query: String,
        @Part("street_poi_id") streetId: Int?
    ): Response<SearchResponse>

    @GET("v1/route/{from}/to/{to}")
    suspend fun getRoute(
        @Path("from") from: String,
        @Path("to") to: String
    ): Response<RouteResponse>
}











