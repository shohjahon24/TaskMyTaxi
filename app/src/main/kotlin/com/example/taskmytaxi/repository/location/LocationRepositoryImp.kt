package com.example.taskmytaxi.repository.location

import android.util.Log
import android.widget.Toast
import com.example.taskmytaxi.datasource.network.ClientResponse
import com.example.taskmytaxi.datasource.network.LocationService
import com.example.taskmytaxi.datasource.network.model.LocationDtoMapper
import com.example.taskmytaxi.datasource.network.model.RouteDtoMapper
import com.example.taskmytaxi.datasource.network.request.RouteRequest
import com.example.taskmytaxi.datasource.network.request.SearchRequest
import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.domain.model.Point
import com.example.taskmytaxi.domain.model.Route
import java.lang.Exception
import java.lang.NullPointerException

class LocationRepositoryImp(
    private val locationService: LocationService,
    private val locationDtoMapper: LocationDtoMapper,
    private val routeDtoMapper: RouteDtoMapper
) : LocationRepository {
    override suspend fun search(query: String): ClientResponse<List<Location>> {
        try {
            val repo = locationService.search(20, 0.0, 0.0, query, 0)
            if (repo.isSuccessful && repo.body() != null) {
                repo.body()?.let {
                    if (it.status == "success")
                        return ClientResponse.Success(locationDtoMapper.toDomainList(it.data.locations))
                }
            }
            return ClientResponse.Error(NullPointerException().hashCode())

        } catch (ex: Exception) {
            return ClientResponse.Error(ex.hashCode())
        }
    }

    override suspend fun getRoute(pointFrom: Point, pointTo: Point): ClientResponse<Route> {
        try {
            val to = "${pointTo.lat},${pointTo.lng}"
            val from = "${pointFrom.lat},${pointFrom.lng}"
            val repo = locationService.getRoute(from, to)
            Log.d("TAG", "getRoute: ${repo.body()}")
            if (repo.isSuccessful && repo.body() != null) {
                repo.body()?.let {
                    return ClientResponse.Success(routeDtoMapper.mapToDomainModel(it.data.route))
                }
            }
            return ClientResponse.Error(NullPointerException().hashCode())
        } catch (ex: Exception) {
            return ClientResponse.Error(ex.hashCode())
        }
    }
}