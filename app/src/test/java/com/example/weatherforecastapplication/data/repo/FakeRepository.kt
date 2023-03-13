package com.example.weatherforecastapplication.data.repo

import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.LocalAlert
import com.example.weatherforecastapplication.data.model.Root
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*

import org.junit.Test

class FakeRepository(
    private var favoriteList: MutableList<FavouritePlace> = mutableListOf<FavouritePlace>(),
    private var alertList: MutableList<LocalAlert> = mutableListOf<LocalAlert>(),
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

    override suspend fun deleteAlert(alert: LocalAlert) {
        alertList.remove(alert)
    }

    override suspend fun insertAlert(alert: LocalAlert) {
        alertList.add(alert)
    }

    override fun getAllAlerts(): Flow<List<LocalAlert>> = flow {
        emit(alertList)
    }

    override fun insertCurrentWeather(root: Root) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCurrentWeather() {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentWeathers(latLng: LatLng): Flow<Root> = flow {
        emit(weatherResponse)
    }

    override suspend fun getFavouriteWeather(favouritePlace: FavouritePlace): Flow<Root> = flow{
        emit(weatherResponse)
    }
}