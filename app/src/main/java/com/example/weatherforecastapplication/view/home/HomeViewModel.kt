package com.example.weatherforecastapplication.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(var repository: Repository) : ViewModel() {
    private var _root = MutableStateFlow<ApiState>(ApiState.Loading)
    val root = _root.asStateFlow()

    init{
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrentWeathers()
                .catch { e ->
                    _root.value = ApiState.Failure(e)
                }
                .collect {
                    _root.value = ApiState.Success(it)
                }
        }
    }
    fun getCurrentWeather(): StateFlow<ApiState> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrentWeathers()
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