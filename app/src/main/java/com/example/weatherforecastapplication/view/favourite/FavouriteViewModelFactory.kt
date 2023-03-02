package com.example.weatherforecastapplication.view.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapplication.model.Repository

class FavouriteViewModelFactory (
    private val repository: Repository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            FavouriteViewModel(repository) as T
        } else {
            throw IllegalAccessException("ViewModel Class Not Founded")
        }
    }
}