package com.example.weatherforecastapplication.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.productsapp.dataBase.ApiService
import com.example.productsapp.dataBase.RetrofitHelper
import com.ibm.icu.impl.Utility
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.Is
import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.create

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ApiServiceTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var apiObject: ApiService

    @Before
    fun setUp() {
        apiObject = RetrofitHelper.retrofitInstance.create(ApiService::class.java)
    }

    @Test
    fun getRoot_requestApi_isSuccessful() = runBlocking {
        //Given
        val latitude = 33.3.toLong()
        val longtiude = 31.0.toLong()

        //When

        val response = apiObject.getRoot(
                latitude,
                longtiude,
            "bec88e8dd2446515300a492c3862a10e",
            "metric",
            "en")
        // Then
        assertThat(response.code() , Is.`is`(200))
        assertThat(response.body(), IsNull.notNullValue())
        assertThat(response.errorBody(), IsNull.nullValue())
    }
    @Test
    fun getRoot_requestApiWithoutKey_UnAuthorized() = runBlocking {
        //Given
        val latitude = 33.3.toLong()
        val longtiude = 31.0.toLong()
        val apiKey = " "

        //When

        val response = apiObject.getRoot(
                latitude,
                longtiude,
            apiKey,
            "metric",
            "en")
        // Then
        assertThat(response.code() , Is.`is`(401))
        assertThat(response.body(), IsNull.nullValue())
        assertThat(response.errorBody(), IsNull.notNullValue())
    }
}