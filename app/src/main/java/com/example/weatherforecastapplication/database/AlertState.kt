package com.example.weatherforecastapplication.database

import com.example.weatherforecastapplication.model.LocalAlert

sealed class AlertState {
    class Success(val data: List<LocalAlert>) : AlertState()
    class Failure(val msg: Throwable) : AlertState()
    object Loading : AlertState()
}