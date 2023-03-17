package com.example.weatherforecastapplication.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.data.network.ApiState
import com.example.weatherforecastapplication.data.repo.RepositoryInterface
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(var repository: RepositoryInterface) : ViewModel() {
    private var _root = MutableStateFlow<ApiState>(ApiState.Loading)
    val root = _root.asStateFlow()

    fun getCurrentWeather(latLng: LatLng): StateFlow<ApiState> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrentWeathers(latLng)
                .catch { e ->
                    _root.value = ApiState.Failure(e)
                }
                .collect {
                    _root.value = ApiState.Success(it)
                }
        }
        return root
    }

}