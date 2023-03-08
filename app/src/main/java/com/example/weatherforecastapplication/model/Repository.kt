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
    private suspend fun getRoot(latLng: LatLng) : Flow<Root> = flow {
        updateSharedPreferance()
        if (location == Utility.GPS) {
            apiObject.getRoot(
                latLng.latitude.toLong(),
                latLng.longitude.toLong(),
//                33.3.toLong(),
//                31.0.toLong(),
//            "44c59959fbe6086cb77fb203967bbc0c",
            "bec88e8dd2446515300a492c3862a10e",
//                "d9abb2c1d05c5882e937cffd1ecd4923",
//            "4a059725f93489b95183bbcb8c6829b9",
//                "f112a761188e9c22cdf3eb3a44597b00",
                unit,
                language
            ).body()?.let { emit(it) }
        }else{
            apiObject.getRoot(
                latitude,
                longitude,
//            "44c59959fbe6086cb77fb203967bbc0c",
            "bec88e8dd2446515300a492c3862a10e",
//            "d9abb2c1d05c5882e937cffd1ecd4923",
//            "4a059725f93489b95183bbcb8c6829b9"
//                "f112a761188e9c22cdf3eb3a44597b00",
                unit,
                language
            ).body()?.let { emit(it) }
        }
    }
    suspend fun getFavouriteWeather(favouritePlace: FavouritePlace): Flow<Root> = flow {
    updateSharedPreferance()
        apiObject.getRoot(
            favouritePlace.lat.toLong(),
            favouritePlace.lon.toLong(),
//            "44c59959fbe6086cb77fb203967bbc0c",
            "bec88e8dd2446515300a492c3862a10e",
//            "d9abb2c1d05c5882e937cffd1ecd4923",
//            "4a059725f93489b95183bbcb8c6829b9"
//            "f112a761188e9c22cdf3eb3a44597b00",
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
    suspend fun deleteFavouritePlace(favouritePlace: FavouritePlace) {
        WeatherDatabase
            .getInstance(context)
            .favouritePlaceDAO()
            .deleteFavouritePlace(favouritePlace)
    }

    suspend fun deleteAlert(alert: LocalAlert) {
        WeatherDatabase
            .getInstance(context)
            .alertDAO()
            .deleteAlert(alert)
    }

    suspend fun insertAlert(alert: LocalAlert) {
        WeatherDatabase
            .getInstance(context)
            .alertDAO()
            .insertAlert(alert).also {
                getAllFavouritePlaces()
            }
    }

    fun getAllAlerts() = WeatherDatabase
        .getInstance(context)
        .alertDAO()
        .getAllAlerts()

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

