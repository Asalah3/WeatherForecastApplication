package com.example.weatherforecastapplication.database

import androidx.room.*
import com.example.weatherforecastapplication.model.FavouritePlace
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritePlaceDAO {
    @Query("SELECT * FROM FavouritePlace")
    fun getAllFavouritePlaces(): Flow<List<FavouritePlace>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouritePlace(favouritePlace: FavouritePlace?)

    @Delete
    suspend fun deleteFavouritePlace(favouritePlace: FavouritePlace?)

}
