package com.example.weatherforecastapplication.data.repo

import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.LocalAlert
import com.example.weatherforecastapplication.data.model.Root
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FakeDataSource(
    var favoriteList: MutableList<FavouritePlace> = mutableListOf<FavouritePlace>(),
    private var alertList: MutableList<LocalAlert> = mutableListOf<LocalAlert>(),
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

    override fun insertCurrentWeather(root: Root) {
        rootList.add(root)
    }

    override suspend fun deleteCurrentWeather() {
        rootList.clear()
    }

    override fun getAllAlerts(): Flow<List<LocalAlert>> = flow {
        emit(alertList)
    }

    override suspend fun insertAlert(localAlert: LocalAlert) {
        alertList.add(localAlert)
    }

    override suspend fun deleteAlert(localAlert: LocalAlert) {
        alertList.remove(localAlert)
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