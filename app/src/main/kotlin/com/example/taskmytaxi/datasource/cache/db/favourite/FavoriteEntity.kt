package com.example.taskmytaxi.datasource.cache.db.favourite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite")
data class FavoriteEntity(
    val address: String,
    val formattedAddress: String,
    val description: String,
    val lat: Double,
    val lng: Double,
    val type: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
    )