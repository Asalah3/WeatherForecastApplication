package com.example.weatherforecastapplication.view.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecastapplication.MainCoroutineRule
import com.example.weatherforecastapplication.data.database.RoomState
import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.LocalAlert
import com.example.weatherforecastapplication.data.model.Root
import com.example.weatherforecastapplication.data.network.ApiState
import com.example.weatherforecastapplication.data.repo.FakeRepository
import com.example.weatherforecastapplication.data.repo.RepositoryInterface
import com.example.weatherforecastapplication.view.favourite.FavouriteViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.hamcrest.core.IsNull
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(manifest= Config.NONE)
class HomeViewModelTest  {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


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
    private var weatherResponse: Root = Root(46, 655, 584, "asdjadsk", 565, null, emptyList(), emptyList(), emptyList())
    private lateinit var repository: RepositoryInterface
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun initFavouriteViewModelTest(){
        repository = FakeRepository(favoriteList,alertList,weatherResponse)
        homeViewModel = HomeViewModel(repository)
    }

    @Test
    fun getCurrentWeather() = runBlockingTest{
        //Given
        homeViewModel.getCurrentWeather(LatLng(33.0, 32.0))
        var data : Root = weatherResponse
        //When
        val result = homeViewModel.root.first()
        when(result){
            is ApiState.Success -> {
                data = result.data
            }
            is ApiState.Failure -> {
            }
            else -> {
            }
        }
        //Then
        MatcherAssert.assertThat(data , IsNull.notNullValue())
    }
}