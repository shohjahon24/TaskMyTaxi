package com.example.taskmytaxi.datasource.cache.db.favourite

import androidx.room.Dao
import androidx.room.Query
import com.example.taskmytaxi.datasource.cache.db.BaseDao

@Dao
interface FavoriteDao : BaseDao<FavoriteEntity> {
    @Query("select * from Favorite")
    suspend fun getFavorite(): List<FavoriteEntity>

    @Query("select * from Favorite where id = :id")
    suspend fun getFavoriteById(id: Int): FavoriteEntity

}