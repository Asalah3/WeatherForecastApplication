package com.example.weatherforecastapplication.data.database

import com.example.weatherforecastapplication.data.model.FavouritePlace

sealed class RoomState {
    class Success(val data: List<FavouritePlace>) : RoomState()
    class Failure(val msg: Throwable) : RoomState()
    object Loading : RoomState()
}