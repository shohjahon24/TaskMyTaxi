package com.example.taskmytaxi.repository.favorite

import com.example.taskmytaxi.domain.model.Favorite

interface FavoriteRepository {
    suspend fun getAll(): List<Favorite>
    suspend fun insert(favorite: Favorite)
}