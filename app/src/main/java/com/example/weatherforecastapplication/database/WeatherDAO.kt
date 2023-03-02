package com.example.weatherforecastapplication.database

import androidx.room.*
import com.example.weatherforecastapplication.model.Root
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDAO {
    @Query("SELECT * FROM Root")
    fun getAllCurrentWeathers(): Flow<Root>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertCurrentWeather(root: Root)
    @Query("DELETE FROM Root")
    suspend fun deleteCurrentWeather()
}