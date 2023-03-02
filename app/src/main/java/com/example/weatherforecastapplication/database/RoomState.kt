package com.example.weatherforecastapplication.database

import com.example.weatherforecastapplication.model.FavouritePlace

sealed class RoomState {
    class Success(val data: List<FavouritePlace>) : RoomState()
    class Failure(val msg: Throwable) : RoomState()
    object Loading : RoomState()
}