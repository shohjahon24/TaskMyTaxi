package com.example.taskmytaxi.datasource.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskmytaxi.datasource.cache.db.favourite.FavoriteDao
import com.example.taskmytaxi.datasource.cache.db.favourite.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun getFavoriteDao(): FavoriteDao
}