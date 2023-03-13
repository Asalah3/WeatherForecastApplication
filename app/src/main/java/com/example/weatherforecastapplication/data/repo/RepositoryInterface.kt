package com.example.weatherforecastapplication.data.repo

import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.LocalAlert
import com.example.weatherforecastapplication.data.model.Root
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    suspend fun insertFavouritePlace(favouritePlace: FavouritePlace)
    fun getAllFavouritePlaces(): Flow<List<FavouritePlace>>

    suspend fun deleteFavouritePlace(favouritePlace: FavouritePlace)

    suspend fun deleteAlert(alert: LocalAlert)

    suspend fun insertAlert(alert: LocalAlert)
    fun getAllAlerts(): Flow<List<LocalAlert>>
    fun insertCurrentWeather(root: Root)

    suspend fun deleteCurrentWeather()

    suspend fun getCurrentWeathers(latLng: LatLng): Flow<Root>
    suspend fun getFavouriteWeather(favouritePlace: FavouritePlace): Flow<Root>
}