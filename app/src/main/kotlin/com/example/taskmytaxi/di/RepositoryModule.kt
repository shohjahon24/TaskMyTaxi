package com.example.taskmytaxi.di

import com.example.taskmytaxi.datasource.cache.db.favourite.FavoriteDao
import com.example.taskmytaxi.datasource.cache.db.favourite.FavoriteEntityMapper
import com.example.taskmytaxi.datasource.network.LocationService
import com.example.taskmytaxi.datasource.network.model.LocationDtoMapper
import com.example.taskmytaxi.datasource.network.model.RouteDtoMapper
import com.example.taskmytaxi.repository.favorite.FavoriteRepository
import com.example.taskmytaxi.repository.favorite.FavoriteRepositoryImp
import com.example.taskmytaxi.repository.location.LocationRepository
import com.example.taskmytaxi.repository.location.LocationRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideLocationRepository(
        locationService: LocationService,
        locationDtoMapper: LocationDtoMapper,
        routeDtoMapper: RouteDtoMapper
    ): LocationRepository {
        return LocationRepositoryImp(
            locationService, locationDtoMapper, routeDtoMapper
        )
    }

    @Singleton
    @Provides
    fun provideFavoriteRepository(
        favoriteDao: FavoriteDao,
        mapper: FavoriteEntityMapper,
    ): FavoriteRepository {
        return FavoriteRepositoryImp(
            favoriteDao,
            mapper
        )
    }
}

