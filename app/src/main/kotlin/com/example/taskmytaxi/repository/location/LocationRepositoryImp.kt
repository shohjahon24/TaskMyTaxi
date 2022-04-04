package com.example.taskmytaxi.repository.location

import android.util.Log
import com.example.taskmytaxi.datasource.network.ClientResponse
import com.example.taskmytaxi.datasource.network.LocationService
import com.example.taskmytaxi.datasource.network.model.LocationDtoMapper
import com.example.taskmytaxi.datasource.network.model.RouteDtoMapper
import com.example.taskmytaxi.domain.model.Locations
import com.example.taskmytaxi.domain.model.Point
import com.example.taskmytaxi.domain.model.Route

class LocationRepositoryImp(
    private val locationService: LocationService,
    private val locationDtoMapper: LocationDtoMapper,
    private val routeDtoMapper: RouteDtoMapper
) : LocationRepository {
    override suspend fun search(query: String): ClientResponse<List<Locations>> {
        try {
            val repo = locationService.search(20, 0.0, 0.0, query, 0)
            if (repo.isSuccessful && repo.body() != null) {
                repo.body()?.let { it ->
                    if (it.status == "success") {
                        val locations: ArrayList<Locations> = ArrayList()
                        val data = it.data.locations
                        data.forEach { d ->
                            if (d.addressId == null) {
                                val r = locationService.search(5, 0.0, 0.0, "", d.streetId)
                                if (r.isSuccessful && r.body() != null) {
                                    r.body()?.let {
                                        val l = arrayListOf(d)
                                        l.addAll(it.data.locations)
                                        locations.add(locationDtoMapper.toDomainList(l))
                                    }
                                }
                            }
                            else
                                locations.add(locationDtoMapper.toDomainList(listOf(d)))
                        }
                        return ClientResponse.Success(locations)
                    }
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