package com.example.weatherforecastapplication.view.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.database.AlertState
import com.example.weatherforecastapplication.model.LocalAlert
import com.example.weatherforecastapplication.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertsViewModel(val repository: Repository) : ViewModel() {

    private var _alertList = MutableStateFlow<AlertState>(AlertState.Loading)
    val alertList = _alertList.asStateFlow()

    init {
        getAllAlerts()
    }

    fun getAllAlerts() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllAlerts()
            .catch { e ->
                _alertList.value = AlertState.Failure(e)
            }.collect {
                _alertList.value = AlertState.Success(it)
            }
    }

    fun insertAlert(alert: LocalAlert) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAlert(alert)
        }

    }

    fun deleteAlert(alert: LocalAlert) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlert(alert)
        }
    }
}