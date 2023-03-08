package com.example.weatherforecastapplication.view.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.database.RoomState
import com.example.weatherforecastapplication.model.FavouritePlace
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavouriteViewModel(var repository: Repository) : ViewModel() {

    private var _favouritePlaceList = MutableStateFlow<RoomState>(RoomState.Loading)
    val favouritePlaceList = _favouritePlaceList.asStateFlow()

    private var _root = MutableStateFlow<ApiState>(ApiState.Loading)
    val root = _root.asStateFlow()

    init {
        getAllFavouritePlaces()
    }

    fun insertFavouritePlace(favouritePlace: FavouritePlace) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavouritePlace(favouritePlace)
        }

    }

    fun deleteFavouritePlace(favouritePlace: FavouritePlace) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavouritePlace(favouritePlace)
        }
    }

    fun getFavouriteWeather(favouritePlace: FavouritePlace) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavouriteWeather(favouritePlace)
                .catch { e ->
                    _root.value = ApiState.Failure(e)
                }
                .collect {
                    _root.value = ApiState.Success(it)
                }

        }
    }

    fun getAllFavouritePlaces() = viewModelScope.launch(Dispatchers.IO) {

        repository.getAllFavouritePlaces()
            ?.catch { e ->
                _favouritePlaceList.value = RoomState.Failure(e)
            }?.collect {
                _favouritePlaceList.value = RoomState.Success(it)
            }
    }
}