package com.example.weatherforecastapplication.data.repo

import com.example.weatherforecastapplication.data.model.AlertModel
import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.LocalAlert
import com.example.weatherforecastapplication.data.model.Root
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FakeDataSource(
    var favoriteList: MutableList<FavouritePlace> = mutableListOf<FavouritePlace>(),
    private var alertList: MutableList<AlertModel> = mutableListOf<AlertModel>(),
    private var rootList: MutableList<Root> = mutableListOf<Root>(),
) : DataSource {


    override suspend fun getRoot(
        latitude: Long,
        longitude: Long,
        appid: String,
        units: String,
        lang: String
    ): Response<Root> {
        return Response.success(
            Root(46, 655, 584, "asdjadsk", 565, null, emptyList(), emptyList(), emptyList()),
        )
    }

    override fun getAllCurrentWeathers(): Flow<Root> = flow {
        if (rootList.isEmpty())
            emit(
                Root(46, 655, 584, "asdjadsk", 565, null, emptyList(), emptyList(), emptyList()),
            )
        else
            emit(rootList[0])
    }

    override fun getWeatherAlert(latLng: LatLng): Flow<Root> {
        TODO("Not yet implemented")
    }

    override fun insertCurrentWeather(root: Root) {
        rootList.add(root)
    }

    override suspend fun deleteCurrentWeather() {
        rootList.clear()
    }

    override fun getAllAlerts(): Flow<List<AlertModel>>  = flow{
        emit(alertList)
    }

    override suspend fun deleteAlert(id: Int) {
        alertList.remove(alertList[id])
    }

    override suspend fun getAlert(id: Int): AlertModel {
       return alertList[id]
    }

    override suspend fun insertAlert(alert: AlertModel): Long {
        alertList.add(alert)
        return alert.id?.toLong() ?: 0
    }
    override fun getAllFavouritePlaces(): Flow<List<FavouritePlace>> = flow {
        emit(favoriteList)
    }

    override suspend fun insertFavouritePlace(favouritePlace: FavouritePlace?) {
        favoriteList.add(favouritePlace!!)
    }

    override suspend fun deleteFavouritePlace(favouritePlace: FavouritePlace?) {
        favoriteList.remove(favouritePlace)
    }

}