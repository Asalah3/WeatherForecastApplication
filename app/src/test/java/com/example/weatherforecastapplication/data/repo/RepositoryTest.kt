package com.example.weatherforecastapplication.data.repo

import androidx.test.core.app.ApplicationProvider
import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.LocalAlert
import com.example.weatherforecastapplication.data.model.Root
import org.junit.Before

import org.junit.Test

class RepositoryTest {

    // Fake Data
    private var favoriteList:MutableList<FavouritePlace> = mutableListOf<FavouritePlace>(
        FavouritePlace(65.05,45.5,"dfsfsd","dsfs"),
        FavouritePlace(65.05,45.5,"dfsfsd","dsfs"),
        FavouritePlace(65.05,45.5,"dfsfsd","dsfs"),
        FavouritePlace(65.05,45.5,"dfsfsd","dsfs"),
    )
    private var alertList:MutableList<LocalAlert> = mutableListOf<LocalAlert>(
        LocalAlert("fsdfd",545,545,545),
        LocalAlert("fsdfd",545,545,545),
        LocalAlert("fsdfd",545,545,545),
        LocalAlert("fsdfd",545,545,545),
        LocalAlert("fsdfd",545,545,545),
        LocalAlert("fsdfd",545,545,545),
    )
    private var rootList:MutableList<Root> = mutableListOf<Root>(
        Root(46,655,584,"asdjadsk",565,null, emptyList(), emptyList(), emptyList()),
        Root(46,655,584,"asdjadsk",565,null, emptyList(), emptyList(), emptyList()),
        Root(46,655,584,"asdjadsk",565,null, emptyList(), emptyList(), emptyList()),
        Root(46,655,584,"asdjadsk",565,null, emptyList(), emptyList(), emptyList()),
        Root(46,655,584,"asdjadsk",565,null, emptyList(), emptyList(), emptyList()),
    )

    private lateinit var remoteDataSource: FakeDataSource
    private lateinit var localDataSource: FakeDataSource
    private lateinit var repository: Repository

    @Before
    fun initializeRepository(){
        remoteDataSource = FakeDataSource(favoriteList,alertList, rootList)
        localDataSource = FakeDataSource(favoriteList,alertList, rootList)

        repository = Repository(remoteDataSource = remoteDataSource,localDataSource = localDataSource,
            ApplicationProvider.getApplicationContext())
    }

    @Test
    fun getLanguageSharedPreferences() {
    }

    @Test
    fun setLanguageSharedPreferences() {
    }

    @Test
    fun getUnitsShared() {
    }

    @Test
    fun setUnitsShared() {
    }

    @Test
    fun getLanguage() {
    }

    @Test
    fun setLanguage() {
    }

    @Test
    fun getUnit() {
    }

    @Test
    fun setUnit() {
    }

    @Test
    fun getLocationShared() {
    }

    @Test
    fun setLocationShared() {
    }

    @Test
    fun getLocation() {
    }

    @Test
    fun setLocation() {
    }

    @Test
    fun getLatitudeSharedPreferences() {
    }

    @Test
    fun setLatitudeSharedPreferences() {
    }

    @Test
    fun getLongitudeSharedPreferences() {
    }

    @Test
    fun setLongitudeSharedPreferences() {
    }

    @Test
    fun getLatitude() {
    }

    @Test
    fun setLatitude() {
    }

    @Test
    fun getLongitude() {
    }

    @Test
    fun setLongitude() {
    }

    @Test
    fun getFavouriteWeather() {
    }

    @Test
    fun insertFavouritePlace() {
    }

    @Test
    fun getAllFavouritePlaces() {
    }

    @Test
    fun deleteFavouritePlace() {
    }

    @Test
    fun deleteAlert() {
    }

    @Test
    fun insertAlert() {
    }

    @Test
    fun getAllAlerts() {
    }

    @Test
    fun getCurrentWeathers() {
    }
}