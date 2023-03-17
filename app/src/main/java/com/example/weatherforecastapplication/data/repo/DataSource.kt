package com.example.weatherforecastapplication.data.repo

import com.example.weatherforecastapplication.data.model.AlertModel
import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.Root
import com.google.android.gms.maps.model.LatLng
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
    fun getWeatherAlert(latLng: LatLng): Flow<Root>

    fun insertCurrentWeather(root: Root)

    suspend fun deleteCurrentWeather()

    fun getAllAlerts(): Flow<List<AlertModel>>

    suspend fun insertAlert(alert: AlertModel):Long

    suspend fun deleteAlert(id: Int)

    suspend fun getAlert(id: Int): AlertModel

    fun getAllFavouritePlaces(): Flow<List<FavouritePlace>>

    suspend fun insertFavouritePlace(favouritePlace: FavouritePlace?)

    suspend fun deleteFavouritePlace(favouritePlace: FavouritePlace?)

}