package com.example.weatherforecastapplication.network

import com.example.weatherforecastapplication.model.Root

sealed class ApiState {
    class Success(val data: Root) : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    object Loading : ApiState()
}
