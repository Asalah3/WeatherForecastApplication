package com.example.productsapp.dataBase

import com.example.weatherforecastapplication.model.Root
import kotlinx.coroutines.flow.Flow
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
}

object RetrofitHelper {
    const val BASE_URL =
        "https://api.openweathermap.org/data/2.5/"
    val retrofitInstance = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}