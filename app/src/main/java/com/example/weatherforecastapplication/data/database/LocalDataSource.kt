package com.example.weatherforecastapplication.data.database

import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.LocalAlert
import com.example.weatherforecastapplication.data.model.Root
import com.example.weatherforecastapplication.data.repo.DataSource
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

   /* override  fun getRootApi(
        latitude: Long,
        longitude: Long,
        appid: String,
        lang: String
    ): Root {
        TODO("Not yet implemented")
    }
*/
    override fun getAllCurrentWeathers(): Flow<Root> {
        return weatherDAO.getAllCurrentWeathers()
    }

    override fun insertCurrentWeather(root: Root) {
        weatherDAO.insertCurrentWeather(root)
    }

    override suspend fun deleteCurrentWeather() {
        weatherDAO.deleteCurrentWeather()
    }

    override fun getAllAlerts(): Flow<List<LocalAlert>> {
        return alertDAO.getAllAlerts()
    }

    override suspend fun insertAlert(localAlert: LocalAlert) {
        alertDAO.insertAlert(localAlert)
    }

    override suspend fun deleteAlert(localAlert: LocalAlert) {
        alertDAO.deleteAlert(localAlert)
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