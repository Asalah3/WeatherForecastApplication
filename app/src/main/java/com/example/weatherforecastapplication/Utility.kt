package com.example.weatherapp.ui.home.view


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherforecastapplication.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Utility {

    companion object {
        val latLongSharedPrefKey: String = "LatLong"
        val GPSLatKey: String = "GPSLat"
        val GPSLongKey: String = "GPSLong"
        val Language_EN_Value: String = "en"
        val Language_AR_Value: String = "ar"
        val Language_Key: String = "Lang"
        val Language_Value_Key = "Language"
        val TEMP_KEY = "Temp"
        val IMPERIAL = "imperial"
        val STANDARD = "standard"
        val METRIC = "metric"
        fun timeStampToDay(dt: Long): String {
            var date: Date = Date(dt * 1000)
            var dateFormat: DateFormat = SimpleDateFormat("EEEE")
            return dateFormat.format(date)
        }

        fun timeStampToHour(dt: Long): String {
            var date: Date = Date(dt * 1000)
            var dateFormat: DateFormat = SimpleDateFormat("h:mm a")
            return dateFormat.format(date)
        }

        fun saveLanguageToSharedPref(context: Context, key: String, value: String) {
            var editor: SharedPreferences.Editor = context.getSharedPreferences(
                "Language",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putString(key, value)
            editor.apply()
        }
        fun saveTempToSharedPref(context: Context, key : String, value : String){
            var editor : SharedPreferences.Editor = context.getSharedPreferences("Units",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putString(key, value)
            editor.apply()
        }
        fun getWeatherIcon(imageString: String): Int {
            val imageInInteger: Int
            when (imageString) {
                "01d" -> imageInInteger = R.drawable.icon_01d
                "01n" -> imageInInteger = R.drawable.icon_01n
                "02d" -> imageInInteger = R.drawable.icon_02d
                "02n" -> imageInInteger = R.drawable.icon_02n
                "03n" -> imageInInteger = R.drawable.icon_03n
                "03d" -> imageInInteger = R.drawable.icon_03d
                "04d" -> imageInInteger = R.drawable.icon_04d
                "04n" -> imageInInteger = R.drawable.icon_04n
                "09d" -> imageInInteger = R.drawable.icon_09d
                "09n" -> imageInInteger = R.drawable.icon_09n
                "10d" -> imageInInteger = R.drawable.icon_10d
                "10n" -> imageInInteger = R.drawable.icon_10n
                "11d" -> imageInInteger = R.drawable.icon_11d
                "11n" -> imageInInteger = R.drawable.icon_11n
                "13d" -> imageInInteger = R.drawable.icon_13d
                "13n" -> imageInInteger = R.drawable.icon_13n
                "50d" -> imageInInteger = R.drawable.icon_50d
                "50n" -> imageInInteger = R.drawable.icon_50n
                else -> imageInInteger = R.drawable.cloud_icon
            }
            return imageInInteger
        }
    }

}