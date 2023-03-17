package com.example.weatherforecastapplication.data.database

import androidx.room.*
import com.example.weatherforecastapplication.data.model.AlertModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDAO {
    @Query("SELECT * FROM alert")
    fun getAllAlerts(): Flow<List<AlertModel>>

    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(localAlert: LocalAlert)

    @Delete
    suspend fun deleteAlert(localAlert: LocalAlert)*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertModel):Long

    @Query("DELETE FROM alert WHERE id = :id")
    suspend fun deleteAlert(id: Int)

    @Query("SELECT * FROM alert WHERE id = :id")
    suspend fun getAlert(id: Int):AlertModel
}