package com.example.taskmytaxi.di

import android.content.Context
import androidx.room.Room
import com.example.taskmytaxi.datasource.cache.db.AppDb
import com.example.taskmytaxi.datasource.cache.db.favourite.FavoriteDao
import com.example.taskmytaxi.datasource.cache.db.favourite.FavoriteEntityMapper
import com.example.taskmytaxi.datasource.network.model.LocationDtoMapper
import com.example.taskmytaxi.datasource.network.model.PointDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideAppDb(@ApplicationContext context: Context): AppDb =
        Room.databaseBuilder(context, AppDb::class.java, "mytaxi.db")
            .allowMainThreadQueries()
            .build()

    @Provides
    fun provideFavoriteDao(db: AppDb): FavoriteDao = db.getFavoriteDao()

    @Singleton
    @Provides
    fun provideFavoriteMapper(): FavoriteEntityMapper {
        return FavoriteEntityMapper()
    }

}