package com.example.taskmytaxi.datasource.cache.db.favourite

import com.example.taskmytaxi.domain.model.Favorite
import com.example.taskmytaxi.domain.model.Point
import com.example.taskmytaxi.domain.util.DomainMapper

class FavoriteEntityMapper : DomainMapper<FavoriteEntity, Favorite> {
    override fun mapToDomainModel(model: FavoriteEntity): Favorite {
        return Favorite(
            address = model.address,
            formattedAddress = model.formattedAddress,
            description = model.description,
            point = Point(model.lat, model.lng),
            type = model.type
        )
    }

    override fun mapFromDomainModel(domainModel: Favorite): FavoriteEntity {
        return FavoriteEntity(
            address = domainModel.address,
            formattedAddress = domainModel.formattedAddress,
            description = domainModel.description,
            lat = domainModel.point.lat,
            lng = domainModel.point.lng,
            type = domainModel.type
        )
    }


    fun toDomainList(initial: List<FavoriteEntity>): List<Favorite> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Favorite>): List<FavoriteEntity> {
        return initial.map { mapFromDomainModel(it) }
    }
}