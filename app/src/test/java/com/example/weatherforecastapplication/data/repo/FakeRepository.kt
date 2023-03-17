package com.example.weatherforecastapplication.data.repo

import com.example.weatherforecastapplication.data.model.AlertModel
import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.Root
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository(
    private var favoriteList: MutableList<FavouritePlace> = mutableListOf<FavouritePlace>(),
    private var alertList: MutableList<AlertModel> = mutableListOf<AlertModel>(),
    private var weatherResponse: Root = Root(
        46,
        655,
        584,
        "asdjadsk",
        565,
        null,
        emptyList(),
        emptyList(),
        emptyList()
    )
) : RepositoryInterface {

    override suspend fun insertFavouritePlace(favouritePlace: FavouritePlace) {
        favoriteList.add(favouritePlace)
    }

    override fun getAllFavouritePlaces(): Flow<List<FavouritePlace>> = flow {
        emit(favoriteList)
    }

    override suspend fun deleteFavouritePlace(favouritePlace: FavouritePlace) {
        favoriteList.remove(favouritePlace)
    }

    override suspend fun deleteAlert(id: Int) {
        alertList.remove(alertList[id])
    }

    override suspend fun insertAlert(alert: AlertModel): Long {
         alertList.add(alert)
        return alert.id?.toLong() ?: 0
    }

    override fun getAllAlerts(): Flow<List<AlertModel>> = flow {
        emit(alertList)
    }

    override suspend fun getAlert(id: Int): AlertModel {
        TODO("Not yet implemented")
    }

    override fun insertCurrentWeather(root: Root) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCurrentWeather() {
        TODO("Not yet implemented")
    }

    override suspend fun getRoot(latLng: LatLng): Flow<Root> = flow{
        emit(weatherResponse)
    }

    override suspend fun getWeatherAlert(latLng: LatLng): Flow<Root> = flow{
        emit(weatherResponse)
    }

    override suspend fun getCurrentWeathers(latLng: LatLng): Flow<Root> = flow {
        emit(weatherResponse)
    }

    override suspend fun getFavouriteWeather(favouritePlace: FavouritePlace): Flow<Root> = flow{
        emit(weatherResponse)
    }
}