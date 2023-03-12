package com.example.weatherforecastapplication.data.repo

import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.LocalAlert
import com.example.weatherforecastapplication.data.model.Root
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface DataSource {

    suspend fun getRoot(
        latitude: Long,
        longitude: Long,
        appid: String,
        units: String,
        lang: String
    ): Response<Root>
     /*fun getRootApi(
        latitude: Long,
        longitude: Long,
        appid: String,
        lang: String
    ):Root*/
    fun getAllCurrentWeathers(): Flow<Root>

    fun insertCurrentWeather(root: Root)

    suspend fun deleteCurrentWeather()

    fun getAllAlerts(): Flow<List<LocalAlert>>

    suspend fun insertAlert(localAlert: LocalAlert)

    suspend fun deleteAlert(localAlert: LocalAlert)

    fun getAllFavouritePlaces(): Flow<List<FavouritePlace>>

    suspend fun insertFavouritePlace(favouritePlace: FavouritePlace?)

    suspend fun deleteFavouritePlace(favouritePlace: FavouritePlace?)

}