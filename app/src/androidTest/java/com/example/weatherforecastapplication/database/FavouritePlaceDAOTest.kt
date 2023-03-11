package com.example.weatherforecastapplication.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherforecastapplication.data.database.FavouritePlaceDAO
import com.example.weatherforecastapplication.data.database.WeatherDatabase
import com.example.weatherforecastapplication.data.model.FavouritePlace
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.Is
import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavouritePlaceDAOTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var db : WeatherDatabase
    lateinit var dao : FavouritePlaceDAO
    @Before
    fun setUp() {
        // Initialize the DataBase
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.favouritePlaceDAO()

    }

    @After
    fun tearDown() {
        // Close the DataBase
        db.close()
    }

    @Test
    fun getAllFavouritePlaces_insertFavouritePlaces_countOfItemSame() = runBlockingTest {
        //Given
        val data1 = FavouritePlace(lat = 33.00 , lon = 32.00 , addressName = "Ismailia" , countryName = "Egypt")
        val data2 = FavouritePlace(lat = 32.00 , lon = 32.00 , addressName = "Cairo" , countryName = "Egypt")
        val data3 = FavouritePlace(lat = 34.00 , lon = 32.00 , addressName = "Alex" , countryName = "Egypt")

        dao.insertFavouritePlace(data1)
        dao.insertFavouritePlace(data2)
        dao.insertFavouritePlace(data3)

        //when
        val result = dao.getAllFavouritePlaces().first()

        //Then
        MatcherAssert.assertThat(result.size , Is.`is`(3))
    }

    @Test
    fun insertFavouritePlace_insertSingleItem_returnItem()= runBlockingTest {
        //Given
        val data1 = FavouritePlace(lat = 33.00 , lon = 32.00 , addressName = "Ismailia" , countryName = "Egypt")

        //when
        dao.insertFavouritePlace(data1)

        //Then
        val result = dao.getAllFavouritePlaces().first()
        MatcherAssert.assertThat(result[0], IsNull.notNullValue())
    }

    @Test
    fun deleteFavouritePlace_deleteItem_checkIsNull()  = runBlockingTest{
        // Given
        val data1 = FavouritePlace(lat = 33.00 , lon = 32.00 , addressName = "Ismailia" , countryName = "Egypt")
        dao.insertFavouritePlace(data1)
        val outComData = dao.getAllFavouritePlaces().first()

        //when
        dao.deleteFavouritePlace(outComData[0])

        //Then
        val result = dao.getAllFavouritePlaces().first()
        assertThat(result, IsEmptyCollection.empty())
        assertThat(result.size , Is.`is`(0))
    }
}