package com.example.weatherforecastapplication.data.repo

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherforecastapplication.data.network.ApiService
import com.example.weatherforecastapplication.data.network.RetrofitHelper
import com.example.weatherapp.ui.home.view.Utility
import com.example.weatherforecastapplication.data.database.LocalDataSource
import com.example.weatherforecastapplication.data.database.WeatherDatabase
import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.LocalAlert
import com.example.weatherforecastapplication.data.model.Root
import com.example.weatherforecastapplication.data.network.RemoteDataSource
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow



class Repository (
    private val remoteDataSource: DataSource,
    private val localDataSource: DataSource,
    private val context: Context
    ) {

    companion object{
        private var INSTANCE: Repository?=null

        fun getInstance(app:Application):Repository{
            return INSTANCE?: synchronized(this){
                val room = WeatherDatabase.getInstance(app)
                val retrofit = RetrofitHelper.retrofitInstance
                val api = retrofit.create(ApiService::class.java)
                val localDataSource = LocalDataSource(
                    favouritePlaceDAO = room.favouritePlaceDAO(),
                    alertDAO = room.alertDAO(),
                    weatherDAO = room.weatherDAO()
                )

                val remoteDataSource = RemoteDataSource(api = api)

                Repository(
                    remoteDataSource = remoteDataSource,
                    localDataSource = localDataSource,
                    app.applicationContext
                )

            }
        }
    }


    lateinit var languageSharedPreferences:SharedPreferences
    lateinit var unitsShared : SharedPreferences
    lateinit var language:String
    lateinit var unit:String
    lateinit var locationShared: SharedPreferences
    lateinit var location :String
    lateinit var latitudeSharedPreferences: SharedPreferences
    lateinit var longitudeSharedPreferences: SharedPreferences
    var latitude: Double =0.0
    var longitude : Double =0.0

    private fun updateSharedPreferance(){
        languageSharedPreferences =
            context.getSharedPreferences(Utility.Language_Value_Key, Context.MODE_PRIVATE)
        unitsShared  = context.getSharedPreferences("Units", AppCompatActivity.MODE_PRIVATE)
        locationShared =
            context.getSharedPreferences(Utility.LOCATION_KEY, Context.MODE_PRIVATE)
        location = locationShared.getString(Utility.LOCATION_KEY, Utility.GPS)!!

        latitudeSharedPreferences =
            context.getSharedPreferences(Utility.LATITUDE_KEY, Context.MODE_PRIVATE)
        longitudeSharedPreferences =
            context.getSharedPreferences(Utility.LONGITUDE_KEY, Context.MODE_PRIVATE)
        language = languageSharedPreferences.getString(Utility.Language_Key, "en")!!
        unit = unitsShared.getString(Utility.TEMP_KEY,"metric")!!
        latitude = latitudeSharedPreferences.getString(Utility.LATITUDE_KEY, "0.0")!!.toDouble()
        longitude = longitudeSharedPreferences.getString(Utility.LONGITUDE_KEY,"0.0")!!.toDouble()


    }
    private suspend fun getRoot(latLng: LatLng) : Flow<Root> = flow {
        updateSharedPreferance()
        if (location == Utility.GPS) {
            remoteDataSource.getRoot(
                latLng.latitude.toLong(),
                latLng.longitude.toLong(),
//                33.3.toLong(),
//                31.0.toLong(),
//            "44c59959fbe6086cb77fb203967bbc0c",
//            "bec88e8dd2446515300a492c3862a10e",
                "d9abb2c1d05c5882e937cffd1ecd4923",
//            "4a059725f93489b95183bbcb8c6829b9",
//                "f112a761188e9c22cdf3eb3a44597b00",
                unit,
                language
            ).body()?.let { emit(it) }
        }else{
            remoteDataSource.getRoot(
                latitude.toLong(),
                longitude.toLong(),
//            "44c59959fbe6086cb77fb203967bbc0c",
//            "bec88e8dd2446515300a492c3862a10e",
            "d9abb2c1d05c5882e937cffd1ecd4923",
//            "4a059725f93489b95183bbcb8c6829b9"
//                "f112a761188e9c22cdf3eb3a44597b00",
                unit,
                language
            ).body()?.let { emit(it) }
        }
    }


    /*  fun getRootApi(latLng: LatLng) :Root{
        return    remoteDataSource.getRootApi(
                latitude,
                longitude,
//            "44c59959fbe6086cb77fb203967bbc0c",
                "bec88e8dd2446515300a492c3862a10e",
//            "d9abb2c1d05c5882e937cffd1ecd4923",
//            "4a059725f93489b95183bbcb8c6829b9"
//                "f112a761188e9c22cdf3eb3a44597b00",
                "en"
            )
    }*/
    suspend fun getFavouriteWeather(favouritePlace: FavouritePlace): Flow<Root> = flow {
    updateSharedPreferance()
        remoteDataSource.getRoot(
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
        localDataSource
            .insertFavouritePlace(favouritePlace).also {
                getAllFavouritePlaces()
            }
    }

    fun getAllFavouritePlaces() = localDataSource
        .getAllFavouritePlaces()
    suspend fun deleteFavouritePlace(favouritePlace: FavouritePlace) {
        localDataSource
            .deleteFavouritePlace(favouritePlace)
    }

    suspend fun deleteAlert(alert: LocalAlert) {
        localDataSource
            .deleteAlert(alert)
    }

    suspend fun insertAlert(alert: LocalAlert) {
        localDataSource
            .insertAlert(alert).also {
                getAllFavouritePlaces()
            }
    }

    fun getAllAlerts() = localDataSource
        .getAllAlerts()

    fun insertCurrentWeather(root: Root) {
        localDataSource
            .insertCurrentWeather(root)
    }

    suspend fun deleteCurrentWeather() {
        localDataSource
            .deleteCurrentWeather()
    }

    private fun getAllCurrentWeathers() = localDataSource
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

