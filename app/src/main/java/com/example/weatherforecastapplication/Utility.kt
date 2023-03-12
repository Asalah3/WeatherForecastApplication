package com.example.weatherapp.ui.home.view


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherforecastapplication.R
import java.text.DateFormat
import java.text.ParseException
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
        val LOCATION_KEY = "Location"
        val MAP = "map"
        val GPS = "gps"
        val LATITUDE_KEY = "Latitude"
        val LONGITUDE_KEY = "Longitude"
        fun timeStampToDay(dt: Long): String {
            var date: Date = Date(dt * 1000)
            var dateFormat: DateFormat = SimpleDateFormat("EEEE")
            return dateFormat.format(date)
        }

        fun timeStampToHour(dt: Long): String {
            var date: Date = Date(dt * 1000)
            var dateFormat: DateFormat = SimpleDateFormat("h:mm aa")
            return dateFormat.format(date)
        }
        fun timeStampToDate (dt : Long) : String{
            var date : Date = Date(dt)
            var dateFormat : DateFormat = SimpleDateFormat("MMM d, yyyy")
            return dateFormat.format(date)
        }
        fun dateToLong(date: String?): Long {
            val f = SimpleDateFormat("dd-MM-yyyy")
            var milliseconds: Long = 0
            try {
                val d = f.parse(date)
                milliseconds = d.time
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return milliseconds
        }
        fun timeToMillis(hour: Int, min: Int): Long {
            return ((hour * 60 + min) * 60 * 1000).toLong()
        }

        fun saveLanguageToSharedPref(context: Context, key: String, value: String) {
            var editor: SharedPreferences.Editor = context.getSharedPreferences(
                "Language",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putString(key, value)
            editor.apply()
        }
        fun saveLatitudeToSharedPref(context: Context, key: String, value: String) {
            var editor: SharedPreferences.Editor = context.getSharedPreferences(
                LATITUDE_KEY,
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putString(key, value)
            editor.apply()
        }
        fun saveLongitudeToSharedPref(context: Context, key: String, value: String) {
            var editor: SharedPreferences.Editor = context.getSharedPreferences(
                LONGITUDE_KEY,
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun saveTempToSharedPref(context: Context, key: String, value: String) {
            var editor: SharedPreferences.Editor = context.getSharedPreferences(
                "Units",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun saveLocationSharedPref(context: Context, key : String, value : String){
            var editor : SharedPreferences.Editor = context.getSharedPreferences(LOCATION_KEY,
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putString(key, value)
            editor.apply()
        }
        fun convertNumbersToArabic(value: Int): String {
            return (value.toString() + "")
                .replace("1".toRegex(), "١")
                .replace("2".toRegex(), "٢")
                .replace("3".toRegex(), "٣")
                .replace("4".toRegex(), "٤")
                .replace("5".toRegex(), "٥")
                .replace("6".toRegex(), "٦")
                .replace("7".toRegex(), "٧")
                .replace("8".toRegex(), "٨")
                .replace("9".toRegex(), "٩")
                .replace("0".toRegex(), "٠")
        }

        fun convertNumbersToArabic(value: Long): String {
            return (value.toString() + "")
                .replace("1".toRegex(), "١")
                .replace("2".toRegex(), "٢")
                .replace("3".toRegex(), "٣")
                .replace("4".toRegex(), "٤")
                .replace("5".toRegex(), "٥")
                .replace("6".toRegex(), "٦")
                .replace("7".toRegex(), "٧")
                .replace("8".toRegex(), "٨")
                .replace("9".toRegex(), "٩")
                .replace("0".toRegex(), "٠")
        }

        fun convertNumbersToArabic(value: Double): String {
            return (value.toString() + "")
                .replace("1".toRegex(), "١")
                .replace("2".toRegex(), "٢")
                .replace("3".toRegex(), "٣")
                .replace("4".toRegex(), "٤")
                .replace("5".toRegex(), "٥")
                .replace("6".toRegex(), "٦")
                .replace("7".toRegex(), "٧")
                .replace("8".toRegex(), "٨")
                .replace("9".toRegex(), "٩")
                .replace("0".toRegex(), "٠")
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