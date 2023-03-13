package com.example.weatherforecastapplication.view.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapplication.data.repo.Repository
import com.example.weatherforecastapplication.data.repo.RepositoryInterface

class FavouriteViewModelFactory (
    private val repository: RepositoryInterface
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