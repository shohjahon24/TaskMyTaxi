package com.example.taskmytaxi.datasource.cache.db.favourite

import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.domain.model.Point
import com.example.taskmytaxi.domain.util.DomainMapper

class FavoriteEntityMapper : DomainMapper<FavoriteEntity, Location> {
    override fun mapToDomainModel(model: FavoriteEntity): Location {
        return Location(
            address = model.address,
            formattedAddress = model.formattedAddress,
            point = Point(model.lat, model.lng),
            streetId = 0,
            addressId = 0,
            lang = "",
            type = model.type
        )
    }

    override fun mapFromDomainModel(domainModel: Location): FavoriteEntity {
        return FavoriteEntity(
            address = domainModel.address,
            formattedAddress = domainModel.formattedAddress,
            lat = domainModel.point.lat,
            lng = domainModel.point.lng,
            type = domainModel.type
        )
    }


    fun toDomainList(initial: List<FavoriteEntity>): List<Location> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Location>): List<FavoriteEntity> {
        return initial.map { mapFromDomainModel(it) }
    }
}