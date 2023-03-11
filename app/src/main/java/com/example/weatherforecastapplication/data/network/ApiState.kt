package com.example.weatherforecastapplication.data.network

import com.example.weatherforecastapplication.data.model.Root

sealed class ApiState {
    class Success(val data: Root) : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    object Loading : ApiState()
}
