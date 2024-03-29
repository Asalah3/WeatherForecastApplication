package com.example.weatherforecastapplication.view.alerts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecastapplication.MainCoroutineRule
import com.example.weatherforecastapplication.data.database.AlertState
import com.example.weatherforecastapplication.data.model.AlertModel
import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.Root
import com.example.weatherforecastapplication.data.repo.FakeRepository
import com.example.weatherforecastapplication.data.repo.RepositoryInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(manifest= Config.NONE)
class AlertsViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    // Fake Data
    private var favoriteList: MutableList<FavouritePlace> = mutableListOf<FavouritePlace>(
        FavouritePlace(65.05, 45.5, "dfsfsd"),
        FavouritePlace(65.05, 45.5, "dfsfsd"),
        FavouritePlace(65.05, 45.5, "dfsfsd"),
        FavouritePlace(65.05, 45.5, "dfsfsd"),
    )
    private var alertList: MutableList<AlertModel> = mutableListOf<AlertModel>(
        AlertModel(1,10000,1000000,1000000,100000,32.0,33.0,"fsdfd"),
        AlertModel(1,10000,1000000,1000000,100000,32.0,33.0,"fsdfd"),
        AlertModel(1,10000,1000000,1000000,100000,32.0,33.0,"fsdfd"),
        AlertModel(1,10000,1000000,1000000,100000,32.0,33.0,"fsdfd"),
        AlertModel(1,10000,1000000,1000000,100000,32.0,33.0,"fsdfd"),
        AlertModel(1,10000,1000000,1000000,100000,32.0,33.0,"fsdfd"),
    )
    private var weatherResponse: Root = Root(46, 655, 584, "asdjadsk", 565, null, emptyList(), emptyList(), emptyList())
    private lateinit var repository: RepositoryInterface
    private lateinit var alertsViewModel: AlertsViewModel

    @Before
    fun initFavouriteViewModelTest(){
        repository = FakeRepository(favoriteList,alertList,weatherResponse)
        alertsViewModel = AlertsViewModel(repository)
    }

    @Test
    fun getAllAlerts_checkEqualityOfSize() = runBlockingTest{
        //Given
        alertsViewModel.getAllAlerts()
        var data : List<AlertModel> = emptyList()
        //When
        val result = alertsViewModel.alertList.first()
        when(result){
            is AlertState.Success -> {
                data = result.data
            }
            is AlertState.Failure -> {
            }
            else -> {
            }
        }
        //Then
        MatcherAssert.assertThat(data.size , Is.`is`(6))
    }

    @Test
    fun insertAlert_insertItem_increaseSizeOfList() = runBlockingTest{
        //Given
        alertsViewModel.insertAlert(alertList[0])
        //When
        alertsViewModel.getAllAlerts()
        alertsViewModel.alertList.first()
        //Then
        MatcherAssert.assertThat(alertList.size , Is.`is`(7))
    }

    @Test
    fun deleteAlert_deleteItem_decreaseSizeOfList()= runBlockingTest{
        //Given
        alertsViewModel.deleteAlert(alertList[0])
        //When
        alertsViewModel.getAllAlerts()
        alertsViewModel.alertList.first()
        //Then
        MatcherAssert.assertThat(alertList.size , Is.`is`(5))
    }
}