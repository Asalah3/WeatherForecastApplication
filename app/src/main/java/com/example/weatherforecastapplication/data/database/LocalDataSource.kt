package com.example.weatherforecastapplication.data.database

import com.example.weatherforecastapplication.data.model.AlertModel
import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.Root
import com.example.weatherforecastapplication.data.repo.DataSource
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class LocalDataSource(
    private val alertDAO: AlertDAO,
    private val weatherDAO: WeatherDAO,
    private val favouritePlaceDAO: FavouritePlaceDAO
): DataSource {
    override suspend fun getRoot(
        latitude: Long,
        longitude: Long,
        appid: String,
        units: String,
        lang: String
    ): Response<Root> {
        TODO("Not yet implemented")
    }
    override fun getAllCurrentWeathers(): Flow<Root> {
        return weatherDAO.getAllCurrentWeathers()
    }

    override fun getWeatherAlert(latLng: LatLng): Flow<Root> {
        TODO("Not yet implemented")
    }

    override fun insertCurrentWeather(root: Root) {
        weatherDAO.insertCurrentWeather(root)
    }

    override suspend fun deleteCurrentWeather() {
        weatherDAO.deleteCurrentWeather()
    }

    override fun getAllAlerts(): Flow<List<AlertModel>> {
        return alertDAO.getAllAlerts()
    }

    override suspend fun insertAlert(alert: AlertModel): Long {
        return alertDAO.insertAlert(alert)
    }

    override suspend fun deleteAlert(id: Int) {
        alertDAO.deleteAlert(id)
    }

    override suspend fun getAlert(id: Int): AlertModel {
        return alertDAO.getAlert(id)
    }

    override fun getAllFavouritePlaces(): Flow<List<FavouritePlace>> {
        return favouritePlaceDAO.getAllFavouritePlaces()
    }

    override suspend fun insertFavouritePlace(favouritePlace: FavouritePlace?) {
       favouritePlaceDAO.insertFavouritePlace(favouritePlace)
    }

    override suspend fun deleteFavouritePlace(favouritePlace: FavouritePlace?) {
        favouritePlaceDAO.deleteFavouritePlace(favouritePlace)
    }
}