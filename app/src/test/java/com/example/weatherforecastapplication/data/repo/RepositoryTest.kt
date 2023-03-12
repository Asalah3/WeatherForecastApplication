package com.example.weatherforecastapplication.data.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.LocalAlert
import com.example.weatherforecastapplication.data.model.Root
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Fake Data
    private var favoriteList: MutableList<FavouritePlace> = mutableListOf<FavouritePlace>(
        FavouritePlace(65.05, 45.5, "dfsfsd", "dsfs"),
        FavouritePlace(65.05, 45.5, "dfsfsd", "dsfs"),
        FavouritePlace(65.05, 45.5, "dfsfsd", "dsfs"),
        FavouritePlace(65.05, 45.5, "dfsfsd", "dsfs"),
    )
    private var alertList: MutableList<LocalAlert> = mutableListOf<LocalAlert>(
        LocalAlert("fsdfd", 545, 545, 545),
        LocalAlert("fsdfd", 545, 545, 545),
        LocalAlert("fsdfd", 545, 545, 545),
        LocalAlert("fsdfd", 545, 545, 545),
        LocalAlert("fsdfd", 545, 545, 545),
        LocalAlert("fsdfd", 545, 545, 545),
    )
    private var rootList: MutableList<Root> = mutableListOf<Root>(
        Root(46, 655, 584, "asdjadsk", 565, null, emptyList(), emptyList(), emptyList()),
        Root(46, 655, 584, "asdjadsk", 565, null, emptyList(), emptyList(), emptyList()),
        Root(46, 655, 584, "asdjadsk", 565, null, emptyList(), emptyList(), emptyList()),
        Root(46, 655, 584, "asdjadsk", 565, null, emptyList(), emptyList(), emptyList()),
        Root(46, 655, 584, "asdjadsk", 565, null, emptyList(), emptyList(), emptyList()),
    )

    private lateinit var remoteDataSource: FakeDataSource
    private lateinit var localDataSource: FakeDataSource
    private lateinit var repository: Repository

    @Before
    fun initializeRepository() {
        remoteDataSource = FakeDataSource(favoriteList, alertList, rootList)
        localDataSource = FakeDataSource(favoriteList, alertList, rootList)
        repository = Repository(
            remoteDataSource = remoteDataSource, localDataSource = localDataSource,
            ApplicationProvider.getApplicationContext()
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertFavoritePlaces_insertItem_increaseSizeOfList() = runBlockingTest {
        //Given single item of favorite
        val item = FavouritePlace(43.0, 32.0, "Ismailia", "Egypt")
        //when insert favorite in room in repository
        repository.insertFavouritePlace(item)
        //Then size of favorite list will be 5
        MatcherAssert.assertThat(favoriteList.size, Is.`is`(5))
    }

    @Test
    fun deleteFromFav_deleteItem_decreaseSizeOfTest() = runBlockingTest {
        //Given delete single  item of favorite
        val item = favoriteList[0]
        //when delete favorite in room in repository
        repository.deleteFavouritePlace(item)
        //Then size of favorite list will be 3
        MatcherAssert.assertThat(favoriteList.size, Is.`is`(3))

    }

    @Test
    fun getFavoritePlaces_nothing_resultOfFavoriteListIsSameSize() = runBlockingTest {
        //Given

        // when request all  favorite in room in repository
        val result = repository.getAllFavouritePlaces().first()

        //Then size of favorite list will be same size  3
        MatcherAssert.assertThat(result.size, Is.`is`(favoriteList.size))
    }

    @Test
    fun getFavoriteWeather_Nothing_WeatherDetails() = runBlockingTest() {
        //Given
        // When request weather details from retrofit in repository
        val result = repository.getFavouriteWeather(
            favouritePlace = FavouritePlace(43.0, 32.0, "Ismailia", "Egypt")
        )
            .first()
        //Then response is same of fake data
        MatcherAssert.assertThat(result, Is.`is`(rootList[0]))
    }

    @Test
    fun getCurrentWeather_Nothing_CurrentWeather() = runBlockingTest {
        //Given
        // When request current weather
        val result = repository.getCurrentWeathers(LatLng(33.0, 32.0)).first()

        //Then response is same of fake data
        MatcherAssert.assertThat(result, Is.`is`(rootList[0]))
    }

    @Test
    fun insertCurrentWeather_insertItem_increaseSizeOfList() = runBlockingTest {
        //Given single item of current weather
        val item = Root(
            2, 20.0.toLong(), 32.0.toLong(), "suze", 55, null, emptyList(), emptyList(), emptyList()
        )

        //when insert current weather in room in repository
        repository.insertCurrentWeather(item)

        //Then size of root  list will be 6
        MatcherAssert.assertThat(rootList.size, Is.`is`(6))
    }

    @Test
    fun deleteCurrentWeather_Nothing_DecreaseSizeOfList() = runBlockingTest {
        //Given
        // when delete current weather in room in repository
        val result = repository.deleteCurrentWeather()
        //Then size of root list will be 0
        MatcherAssert.assertThat(rootList.size, Is.`is`(0))
    }

    @Test
    fun getAlert_Nothing_ReturnAlert() = runBlockingTest {
        //Given
        // When request Alert
        val result = repository.getAllAlerts().first()

        //Then response is same of fake data
        MatcherAssert.assertThat(result, Is.`is`(alertList))

    }

    @Test
    fun insertAlert_InsertItem_IncreaseSize() = runBlockingTest {
        //Given single item of alert
        val item = LocalAlert("Ismailia", 13, 146112, 189522)

        //when insert alert in room in repository
        repository.insertAlert(item)

        //Then size of root  list will be 2
        MatcherAssert.assertThat(alertList.size, Is.`is`(7))

    }

    @Test
    fun deleteAlert() = runBlockingTest {
        //Given
        var item = alertList[0]
        //when delete  alert in room in repository
        val result = repository.deleteAlert(item)
        //Then size of alert list will be 5
        MatcherAssert.assertThat(alertList.size, Is.`is`(5))
    }
}