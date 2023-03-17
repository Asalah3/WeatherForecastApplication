package com.example.weatherforecastapplication.data.network

import com.example.weatherforecastapplication.data.model.Root
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("onecall")
     suspend fun getRoot(
        @Query("lat") latitude: Long,
        @Query("lon") longitude: Long,
        @Query("appid") appid: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): Response<Root>

    /*@GET("onecall")
     fun getRootApi(
        @Query("lat") latitude: Long,
        @Query("lon") longitude: Long,
        @Query("appid") appid: String,
        @Query("lang") lang: String
    ): Root
    @GET("reverse")
    suspend fun getPlaceName(
        @Query("lat") latitude: Double
        , @Query("lon") longitude: Double
        , @Query("appid") appid: String="bec88e8dd2446515300a492c3862a10e"
        //, @Query("appid") appid: String="d9abb2c1d05c5882e937cffd1ecd4923"
        //, @Query("appid") appid: String="f112a761188e9c22cdf3eb3a44597b00"
        //, @Query("appid") appid: String="489da633b031b5fa008c48ee2deaf025"

    ): Response<GeocodingResponseItem>*/
}

object RetrofitHelper {
    const val BASE_URL =
        "https://api.openweathermap.org/data/2.5/"
    const val BASE_URL_PLACE="http://api.openweathermap.org/geo/1.0/"
    val retrofitInstance = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

   /* val retrofitPlace by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL_PLACE)
            .client(client)
            .build()
    }*/
}