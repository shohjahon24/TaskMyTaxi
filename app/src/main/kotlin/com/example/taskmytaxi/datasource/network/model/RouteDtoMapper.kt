package com.example.taskmytaxi.datasource.network.model

import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.domain.model.Route
import com.example.taskmytaxi.domain.util.DomainMapper

class RouteDtoMapper(private val pointDtoMapper: PointDtoMapper) : DomainMapper<RouteDto, Route> {

    override fun mapToDomainModel(model: RouteDto): Route {
        return Route(
            distance = model.distance,
            duration = model.duration,
            durationInSecond = model.durationInSecond,
            points = pointDtoMapper.toDomainList(model.points)
        )
    }

    override fun mapFromDomainModel(domainModel: Route): RouteDto {
        return RouteDto(
            distance = domainModel.distance,
            duration = domainModel.duration,
            points = pointDtoMapper.fromDomainList(domainModel.points),
            durationInSecond = domainModel.durationInSecond
        )
    }

    fun toDomainList(initial: List<RouteDto>): List<Route> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Route>): List<RouteDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}
