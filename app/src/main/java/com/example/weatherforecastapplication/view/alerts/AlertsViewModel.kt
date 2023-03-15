package com.example.weatherforecastapplication.view.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.data.database.AlertState
import com.example.weatherforecastapplication.data.model.AlertModel
import com.example.weatherforecastapplication.data.model.LocalAlert
import com.example.weatherforecastapplication.data.network.ApiState
import com.example.weatherforecastapplication.data.repo.Repository
import com.example.weatherforecastapplication.data.repo.RepositoryInterface
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertsViewModel(val repository: RepositoryInterface) : ViewModel() {

    private var _alertList = MutableStateFlow<AlertState>(AlertState.Loading)
    val alertList = _alertList.asStateFlow()
    private var _root = MutableStateFlow<ApiState>(ApiState.Loading)
    val root = _root.asStateFlow()

    private val _stateInsetAlert = MutableStateFlow<Long>(0)
    val stateInsetAlert: StateFlow<Long>
        get() = _stateInsetAlert

    private val _stateSingleAlert = MutableStateFlow<AlertModel>(AlertModel())
    val stateSingleAlert: StateFlow<AlertModel>
        get() = _stateSingleAlert
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
    fun getCurrentWeather(latLng: LatLng): StateFlow<ApiState> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeatherAlert(latLng)
                .catch { e ->
                    _root.value = ApiState.Failure(e)
                }
                .collect {
                    _root.value = ApiState.Success(it)
                }
        }
        return root
    }

    fun insertAlert(alert: AlertModel){
        viewModelScope.launch {
            // After Insert Model get id
            val id = repository.insertAlert(alert)

            // Pass Id in state flow
            _stateInsetAlert.value = id
        }
    }

    fun deleteAlert(alert: AlertModel){
        viewModelScope.launch {
            // Pass ID of Alert Model
            repository.deleteAlert(alert.id?:-1)
        }
    }

    fun getAlert(id: Int){
        viewModelScope.launch {
            // Get Alert By ID
            val alertModel  = repository.getAlert(id)
            _stateSingleAlert.value = alertModel
        }
    }
}