package com.example.weatherforecastapplication.backgroundServices

import android.content.Context
import androidx.work.*
import com.example.weatherforecastapplication.data.model.AlertModel
import com.example.weatherforecastapplication.data.repo.Repository
import com.example.weatherforecastapplication.view.alerts.AlertViewModelFactory
import com.example.weatherforecastapplication.view.alerts.convertDateToLong
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.first
import java.util.*
import java.util.concurrent.TimeUnit

class AlertPeriodicWorkManger(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    lateinit var alertFactory: AlertViewModelFactory
    val repository = Repository.getInstance(context)

    override suspend fun doWork(): Result {
        if (!isStopped) {
            val id = inputData.getLong("id", -1)
            val lat = inputData.getDouble("lat", 0.0)
            val lon = inputData.getDouble("lon", 0.0)
            getData(id.toInt(), lat, lon)
        }
        return Result.success()
    }

    private suspend fun getData(id: Int, lat: Double, long: Double) {
        // request data from room or network
        val currentWeather = repository.getWeatherAlert(LatLng(lat, long)).first()
        val alert = repository.getAlert(id)

        if (checkTimeLimit(alert)) {
            val delay: Long = getDelay(alert)
            if (currentWeather.alerts.isNullOrEmpty()) {

                alert.countryName?.let {
                    setOneTimeWorkManger(
                        delay,
                        alert.id,
                        currentWeather.current?.weather?.get(0)?.description ?: "",
                        currentWeather.current?.weather?.get(0)?.description ?: "",
                        it

                    )
                }

            } else {
                currentWeather.alerts[0].event.let {
                    alert.countryName?.let { it1 ->
                        setOneTimeWorkManger(
                            delay,
                            alert.id,
                            currentWeather.alerts[0].tags[0] ?: "",
                            it,
                            it1
                        )
                    }
                }
            }
        } else {
            repository.deleteAlert(id)
            WorkManager.getInstance().cancelAllWorkByTag("$id")
        }
    }

    private fun setOneTimeWorkManger(delay: Long, id: Int?, description: String, event: String, countryName :String) {
        val data = Data.Builder()
        data.putString("description", description)
        data.putString("event", event)
        data.putString("countryName", countryName)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()


        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
            AlertOneTimeWorkManger::class.java,
        )
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "$id",
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )
    }

    private fun getDelay(alert: AlertModel): Long {
        val hour =
            TimeUnit.HOURS.toSeconds(Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toLong())
        val minute =
            TimeUnit.MINUTES.toSeconds(Calendar.getInstance().get(Calendar.MINUTE).toLong())
        return alert.startTime!! - ((hour + minute) - (2 * 3600L))
    }

    private fun checkTimeLimit(alert: AlertModel): Boolean {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val date = "$day/${month + 1}/$year"
        val dayNow = convertDateToLong(date, context)
        return (dayNow >= (alert.startDate ?: 0)
                &&
                dayNow <= (alert.endDate ?: 0))
    }
}