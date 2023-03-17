package com.example.weatherforecastapplication.data.network

import com.example.weatherforecastapplication.data.model.AlertModel
import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.Root
import com.example.weatherforecastapplication.data.repo.DataSource
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RemoteDataSource(
    private val api: ApiService
): DataSource {

    override suspend fun getRoot(
        latitude: Long,
        longitude: Long,
        appid: String,
        units: String,
        lang: String
    ): Response<Root> {
        return api.getRoot(
            latitude,longitude,appid,units,lang
        )
    }


    /*override fun getRootApi(
        latitude: Long,
        longitude: Long,
        appid: String,
        lang: String
    ):Root {
        return api.getRootApi(
            latitude,longitude,appid,lang
        )
    }*/

    override fun getAllCurrentWeathers(): Flow<Root> {
        TODO("Not yet implemented")
    }

    override fun getWeatherAlert(latLng: LatLng): Flow<Root> {
        TODO("Not yet implemented")
    }

    override fun insertCurrentWeather(root: Root) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCurrentWeather() {
        TODO("Not yet implemented")
    }

    override fun getAllAlerts(): Flow<List<AlertModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(alert: AlertModel): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getAlert(id: Int): AlertModel {
        TODO("Not yet implemented")
    }


    override fun getAllFavouritePlaces(): Flow<List<FavouritePlace>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFavouritePlace(favouritePlace: FavouritePlace?) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavouritePlace(favouritePlace: FavouritePlace?) {
        TODO("Not yet implemented")
    }
}
