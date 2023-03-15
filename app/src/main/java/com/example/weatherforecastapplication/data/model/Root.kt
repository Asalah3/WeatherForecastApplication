package com.example.weatherforecastapplication.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Root(
    @PrimaryKey
    var id: Int,
    val lat: Long,
    val lon: Long,
    val timezone: String,
    val timezoneOffset: Long,
    val current: Current? = null,
    val hourly: List<Current>,
    val daily: List<Daily>,
    val alerts: List<Alert>?
): Serializable

@Entity
data class FavouritePlace(

    val lat: Double,
    val lon: Double,
    val countryName: String
) :Serializable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var favouriteId: Int = 0
}

data class Current(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    val feelsLike: Double,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val uvi: Double,
    val clouds: Long,
    val visibility: Long,
    val windSpeed: Double,
    val windDeg: Long,
    val windGust: Double,
    val weather: List<Weather>,
    val pop: Double
)
data class GeocodingResponseItem(
    val country: String = "",
    val lat: Double= 0.0,
    @SerializedName("local_names")
    val localNames: LocalNames?=null,
    val lon: Double = 0.0,
    val name: String = ""
)
data class LocalNames(
    val ar: String,
    val ascii: String,
    val de: String,
    val en: String,
    val eo: String,
    val es: String,
    val et: String,
    @SerializedName("feature_name")
    val featureName: String,
    val fr: String,
    val hi: String,
    val hu: String,
    val pl: String,
    val ru: String,
    val zh: String
)
data class Weather(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)

enum class Description {
    BrokenClouds,
    ClearSky,
    FewClouds,
    OvercastClouds,
    ScatteredClouds
}

enum class Icon {
    The01D,
    The01N,
    The02D,
    The02N,
    The03D,
    The03N,
    The04N
}

enum class Main {
    Clear, Clouds
}

data class Daily(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonset: Long,
    val moonPhase: Double,
    val temp: Temp,
    val feelsLike: FeelsLike,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val windSpeed: Double,
    val windDeg: Long,
    val windGust: Double,
    val weather: List<Weather>,
    val clouds: Long,
    val pop: Double,
    val uvi: Double
)

data class FeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class Temp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)
data class Alert(
    val description: String,
    val end: Int,
    val event: String,
    @SerializedName("sender_name")
    val senderName: String,
    val start: Int,
    val tags: List<String>
)
/*@Entity(tableName = "Alert")
data class LocalAlert(
    val lat: Long,
    val lon: Long,
    val countryName: String,
    val time : Long,
    val type: String,
    val endDate: Long,
    val startDate: Long,
):Serializable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}*/
/*@Entity(tableName = "Alert", primaryKeys = ["idHashLongFromLonLatStartStringEndStringAlertType"])
data class LocalAlert(
    val countryName: String,
    val longitude: Double,
    val latitudeString: Double,
    val startDate: String,
    val endDate: String,
    val startDT: Int,
    val idHashLongFromLonLatStartStringEndStringAlertType: Long,
)*/
@Entity(tableName = "Alert")
data class LocalAlert(
    val time: Long,
    val endDate: Long,
    val countryName: String,
    val startDate: Long,
    val lat: Double=0.0,
    val lon: Double=0.0,
   /* @PrimaryKey
    val idHashLongFromLonLatStartStringEndStringAlertType: Long,
*/
){
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
@Entity(tableName = "alert")
data class AlertModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var startTime: Long?=null,
    var endTime: Long?=null,
    var startDate: Long?=null,
    var endDate: Long?=null,
    var latitude: Double?=null,
    var longitude: Double?=null,
    val countryName: String? = "",
    )