package com.example.taskmytaxi.di

import com.example.taskmytaxi.datasource.network.LocationService
import com.example.taskmytaxi.datasource.network.model.LocationDtoMapper
import com.example.taskmytaxi.datasource.network.model.PointDtoMapper
import com.example.taskmytaxi.datasource.network.model.RouteDtoMapper
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideLocationMapper(pointDtoMapper: PointDtoMapper): LocationDtoMapper {
        return LocationDtoMapper(pointDtoMapper)
    }

    @Singleton
    @Provides
    fun provideRouteMapper(pointDtoMapper: PointDtoMapper): RouteDtoMapper {
        return RouteDtoMapper(pointDtoMapper)
    }

    @Singleton
    @Provides
    fun providePointMapper(): PointDtoMapper {
        return PointDtoMapper()
    }

    @Singleton
    @Provides
    fun provideRecipeService(client: OkHttpClient): LocationService {
        return Retrofit.Builder()
            .baseUrl("https://testrouting.mytaxi.uz/v2/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(client)
            .build()
            .create(LocationService::class.java)
    }

    @Provides
    fun provideHttpClient(
        @Named("api_key") apiKey: Pair<String, String>
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request: Request =
                    chain.request().newBuilder().addHeader(apiKey.first, apiKey.second).build()
                chain.proceed(request)
            }

        return builder.build()
    }


    @Singleton
    @Provides
    @Named("api_key")
    fun provideApiKey(): Pair<String, String> {
        return Pair("api_key", "679e096b28bffbb5d6c3a888ecfe8cf46343eee3")
    }

}