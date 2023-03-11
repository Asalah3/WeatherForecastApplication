package com.example.weatherforecastapplication.data.database

import androidx.room.*
import com.example.weatherforecastapplication.data.model.LocalAlert
import kotlinx.coroutines.flow.Flow
@Dao
interface AlertDAO {
    @Query("SELECT * FROM Alert")
    fun getAllAlerts(): Flow<List<LocalAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(localAlert: LocalAlert)

    @Delete
    suspend fun deleteAlert(localAlert: LocalAlert)
}