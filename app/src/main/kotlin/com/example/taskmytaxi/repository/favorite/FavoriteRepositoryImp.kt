package com.example.taskmytaxi.repository.favorite

import com.example.taskmytaxi.datasource.cache.db.favourite.FavoriteDao
import com.example.taskmytaxi.datasource.cache.db.favourite.FavoriteEntityMapper
import com.example.taskmytaxi.domain.model.Favorite

class FavoriteRepositoryImp(
    private val favoriteDao: FavoriteDao,
    private val mapper: FavoriteEntityMapper,
): FavoriteRepository {
    override suspend fun getAll(): List<Favorite> {
        return mapper.toDomainList(favoriteDao.getFavorite())
    }

    override suspend fun insert(favorite: Favorite) {
        favoriteDao.insert(mapper.mapFromDomainModel(favorite))
    }
}