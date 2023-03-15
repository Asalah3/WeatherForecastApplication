package com.example.weatherforecastapplication.data.database

import com.example.weatherforecastapplication.data.model.AlertModel
import com.example.weatherforecastapplication.data.model.LocalAlert

sealed class AlertState {
    class Success(val data: List<AlertModel>) : AlertState()
    class Failure(val msg: Throwable) : AlertState()
    object Loading : AlertState()
}