package com.example.taskmytaxi.datasource.network.model

import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.domain.model.Locations
import com.example.taskmytaxi.domain.util.DomainMapper

class LocationDtoMapper(private val pointDtoMapper: PointDtoMapper) :
    DomainMapper<LocationDto, Location> {

    override fun mapToDomainModel(model: LocationDto): Location {
        return Location(
            address = model.address,
            addressId = model.addressId,
            formattedAddress = model.formattedAddress,
            lang = model.lang,
            point = pointDtoMapper.mapToDomainModel(model.point),
            streetId = model.streetId
        )
    }

    override fun mapFromDomainModel(domainModel: Location): LocationDto {
        return LocationDto(
            address = domainModel.address,
            addressId = domainModel.addressId,
            formattedAddress = domainModel.formattedAddress,
            lang = domainModel.lang,
            point = pointDtoMapper.mapFromDomainModel(domainModel.point),
            streetId = domainModel.streetId
        )
    }

    fun toDomainList(initial: List<LocationDto>): Locations {
        return Locations(data = initial.map { mapToDomainModel(it) })
    }

    fun fromDomainList(initial: List<Location>): List<LocationDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}
