package com.example.weatherforecastapplication.backgroundServices

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class AlertOneTimeWorkManger(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val description = inputData.getString("description")!!
        val event = inputData.getString("event")!!
        val countryName = inputData.getString("countryName")!!
        startAlertService(description , event , countryName)
        return Result.success()
    }

    private fun startAlertService(description: String , event:String , countryName : String) {
        val intent = Intent(applicationContext, AlertService::class.java)
        intent.putExtra("description", description)
        intent.putExtra("event", event)
        intent.putExtra("countryName", countryName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(applicationContext, intent)
        } else {
            applicationContext.startService(intent)
        }
    }
}