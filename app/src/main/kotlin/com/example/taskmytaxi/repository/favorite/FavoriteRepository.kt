package com.example.taskmytaxi.repository.favorite

import com.example.taskmytaxi.domain.model.Location

interface FavoriteRepository {
    suspend fun getAll(): List<Location>
    suspend fun insertAll(favorites: List<Location>)
}