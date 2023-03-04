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
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository(private val context: Context) {
    val apiObject: ApiService =
        RetrofitHelper.retrofitInstance.create(ApiService::class.java)

    lateinit var languageSharedPreferences:SharedPreferences
    lateinit var unitsShared : SharedPreferences
    lateinit var language:String
    lateinit var unit:String
    lateinit var locationShared: SharedPreferences
    lateinit var location :String
    lateinit var latitudeSharedPreferences: SharedPreferences
    lateinit var longitudeSharedPreferences: SharedPreferences
    var latitude: Long =0.0.toLong()
    var longitude : Long =0.0.toLong()

    fun updateSharedPreferance(){
        languageSharedPreferences =
            context.getSharedPreferences(Utility.Language_Value_Key, Context.MODE_PRIVATE)
        unitsShared  = context.getSharedPreferences("Units", AppCompatActivity.MODE_PRIVATE)
        locationShared =
            context.getSharedPreferences(Utility.LOCATION_KEY, Context.MODE_PRIVATE)
        latitudeSharedPreferences =
            context.getSharedPreferences(Utility.LATITUDE_KEY, Context.MODE_PRIVATE)
        longitudeSharedPreferences =
            context.getSharedPreferences(Utility.LONGITUDE_KEY, Context.MODE_PRIVATE)
        language = languageSharedPreferences.getString(Utility.Language_Key, "en")!!
        unit = unitsShared.getString(Utility.TEMP_KEY,"metric")!!
        location = locationShared.getString(Utility.LOCATION_KEY, Utility.GPS)!!
        latitude = latitudeSharedPreferences.getLong(Utility.LATITUDE_KEY, 0.0.toLong())!!
        longitude = longitudeSharedPreferences.getLong(Utility.LONGITUDE_KEY, 0.0.toLong())!!


    }
    suspend fun getRoot( latLng: LatLng) : Flow<Root> = flow {
        updateSharedPreferance()
        if (location == Utility.GPS) {
            apiObject.getRoot(
                latLng.latitude.toLong(),
                latLng.longitude.toLong(),
                "d9abb2c1d05c5882e937cffd1ecd4923",
                unit,
                language
            ).body()?.let { emit(it) }
        }else{
            apiObject.getRoot(
                latitude,
                longitude,
                "d9abb2c1d05c5882e937cffd1ecd4923",
                unit,
                language
            ).body()?.let { emit(it) }
        }
    }
//                "bec88e8dd2446515300a492c3862a10e",
    suspend fun getFavouriteWeather(favouritePlace: FavouritePlace): Flow<Root> = flow {
    updateSharedPreferance()
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

    suspend fun getCurrentWeathers(latLng: LatLng) = if (checkForInternet(context)) {
        getRoot(latLng).also {
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

