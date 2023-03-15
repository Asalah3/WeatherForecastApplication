package com.example.weatherforecastapplication.data.database

import android.content.Context
import androidx.room.*
import com.example.weatherforecastapplication.data.model.*

@Database(entities = [Root::class, FavouritePlace::class, AlertModel::class], version = 4)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDAO(): WeatherDAO
    abstract fun favouritePlaceDAO(): FavouritePlaceDAO
    abstract fun alertDAO(): AlertDAO

    companion object {
        private var INSTANCE: WeatherDatabase? = null

        @Synchronized
        fun getInstance(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, WeatherDatabase::class.java, "weather_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }

        }
    }
}