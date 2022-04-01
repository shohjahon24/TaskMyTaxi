package com.example.taskmytaxi.datasource.network.model

import com.example.taskmytaxi.domain.model.Point
import com.example.taskmytaxi.domain.util.DomainMapper

class PointDtoMapper : DomainMapper<PointDto, Point> {

    override fun mapToDomainModel(model: PointDto): Point {
        return Point(
            lat = model.lat,
            lng = model.lng ?: model.lon!!
        )
    }

    override fun mapFromDomainModel(domainModel: Point): PointDto {
        return PointDto(
            lat = domainModel.lat,
            lng = domainModel.lng,
            lon = domainModel.lng
        )
    }

    fun toDomainList(initial: List<PointDto>): List<Point> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Point>): List<PointDto> {
        return initial.map { mapFromDomainModel(it) }
    }

}
