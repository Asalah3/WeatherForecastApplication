package com.example.weatherforecastapplication.view.favourite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecastapplication.MainCoroutineRule
import com.example.weatherforecastapplication.data.database.RoomState
import com.example.weatherforecastapplication.data.model.AlertModel
import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.model.Root
import com.example.weatherforecastapplication.data.network.ApiState
import com.example.weatherforecastapplication.data.repo.FakeRepository
import com.example.weatherforecastapplication.data.repo.RepositoryInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
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
class FavoriteViewModelTest {

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
    private lateinit var favouriteViewModel: FavouriteViewModel

    @Before
    fun initFavouriteViewModelTest(){
        repository = FakeRepository(favoriteList,alertList,weatherResponse)
        favouriteViewModel = FavouriteViewModel(repository)
    }
    @Test
    fun insertFavouritePlace_insertItem_increaseSizeOfList() = runBlockingTest{
        //Given
        favouriteViewModel.insertFavouritePlace(favouritePlace = favoriteList[0])
        //When
        favouriteViewModel.getAllFavouritePlaces()
        favouriteViewModel.favouritePlaceList.first()
        //Then
        MatcherAssert.assertThat(favoriteList.size , `is` (5))
    }

    @Test
    fun deleteFavouritePlace_deleteItem_decreaseSizeOfList()  = runBlockingTest{
        //Given
        favouriteViewModel.deleteFavouritePlace(favouritePlace = favoriteList[0])
        //When
        favouriteViewModel.getAllFavouritePlaces()
        favouriteViewModel.favouritePlaceList.first()
        //Then
        MatcherAssert.assertThat(favoriteList.size , `is` (3))
    }

    @Test
    fun getFavouriteWeather_checkIfNotNull()= runBlockingTest{
        //Given
        favouriteViewModel.getFavouriteWeather(favouritePlace = favoriteList[0])
        var data : Root = weatherResponse
        //When
        val result = favouriteViewModel.root.first()
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


    @Test
    fun getAllFavouritePlaces_checkEqualityOfSize() = runBlockingTest{
        //Given
        favouriteViewModel.getAllFavouritePlaces()
        var data : List<FavouritePlace> = emptyList()
        //When
        val result = favouriteViewModel.favouritePlaceList.first()
        when(result){
            is RoomState.Success -> {
                data = result.data
            }
            is RoomState.Failure -> {
            }
            else -> {
            }
        }
        //Then
        MatcherAssert.assertThat(data.size , `is` (4))
    }
}