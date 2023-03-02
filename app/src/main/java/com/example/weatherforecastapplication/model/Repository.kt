package com.example.weatherforecastapplication.model

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.example.productsapp.dataBase.ApiService
import com.example.productsapp.dataBase.RetrofitHelper
import com.example.weatherapp.ui.home.view.Utility
import com.example.weatherforecastapplication.database.WeatherDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository(private val context: Context) {
    val apiObject: ApiService =
        RetrofitHelper.retrofitInstance.create(ApiService::class.java)

    var languageSharedPreferences =
        context.getSharedPreferences(Utility.Language_Value_Key, Context.MODE_PRIVATE)
    var unitsShared : SharedPreferences = context.getSharedPreferences("Units", AppCompatActivity.MODE_PRIVATE)
    var language = languageSharedPreferences.getString(Utility.Language_Key, "en")!!
    var unit = unitsShared.getString(Utility.TEMP_KEY,"metric")!!


    suspend fun getRoot() : Flow<Root> = flow {
        apiObject.getRoot(
            31.0.toLong(),
            32.0.toLong(),
            "bec88e8dd2446515300a492c3862a10e",
            unit,
            language
        ).body()?.let { emit(it) }
    }

    suspend fun getFavouriteWeather(favouritePlace: FavouritePlace): Flow<Root> = flow {
        apiObject.getRoot(
            favouritePlace.lat.toLong(),
            favouritePlace.lon.toLong(),
            "bec88e8dd2446515300a492c3862a10e",
            unit,
            language
        ).body().let {
            if (it != null) {
                emit(it)
            }
        }
    }


    suspend fun insertFavouritePlace(favouritePlace: FavouritePlace) {
        WeatherDatabase
            .getInstance(context)
            .favouritePlaceDAO()
            .insertFavouritePlace(favouritePlace).also {
                getAllFavouritePlaces()
            }
    }

    fun getAllFavouritePlaces() = WeatherDatabase
        .getInstance(context)
        .favouritePlaceDAO()
        .getAllFavouritePlaces()

    private fun insertCurrentWeather(root: Root) {
        WeatherDatabase
            .getInstance(context)
            .weatherDAO()
            .insertCurrentWeather(root)
    }

    private suspend fun deleteCurrentWeather() {
        WeatherDatabase
            .getInstance(context)
            .weatherDAO()
            .deleteCurrentWeather()
    }

    suspend fun deleteFavouritePlace(favouritePlace: FavouritePlace) {
        WeatherDatabase
            .getInstance(context)
            .favouritePlaceDAO()
            .deleteFavouritePlace(favouritePlace)
    }

    private fun getAllCurrentWeathers() = WeatherDatabase
        .getInstance(context)
        .weatherDAO()
        .getAllCurrentWeathers()

    suspend fun getCurrentWeathers() = if (checkForInternet(context)) {
        getRoot().also {
            deleteCurrentWeather()
            it.collect {
                insertCurrentWeather(it)
            }
        }
    } else {
        getAllCurrentWeathers()
    }


    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION") return networkInfo.isConnected
        }
    }

}

