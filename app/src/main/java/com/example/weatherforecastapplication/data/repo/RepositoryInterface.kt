package com.example.weatherforecastapplication.data.repo

import com.example.weatherforecastapplication.data.model.AlertModel
import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.LocalAlert
import com.example.weatherforecastapplication.data.model.Root
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface RepositoryInterface {
    suspend fun insertFavouritePlace(favouritePlace: FavouritePlace)
    fun getAllFavouritePlaces(): Flow<List<FavouritePlace>>

    suspend fun deleteFavouritePlace(favouritePlace: FavouritePlace)

    suspend fun deleteAlert(id: Int)
    suspend fun insertAlert(alert: AlertModel): Long
    fun getAllAlerts(): Flow<List<AlertModel>>
    suspend fun getAlert(id: Int):AlertModel
    fun insertCurrentWeather(root: Root)

    suspend fun deleteCurrentWeather()

    suspend fun getRoot(latLng: LatLng): Flow<Root>
    suspend fun getWeatherAlert(latLng: LatLng): Flow<Root>
    suspend fun getCurrentWeathers(latLng: LatLng): Flow<Root>
    suspend fun getFavouriteWeather(favouritePlace: FavouritePlace): Flow<Root>
}