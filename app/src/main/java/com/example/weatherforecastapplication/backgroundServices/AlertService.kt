package com.example.weatherforecastapplication.backgroundServices

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.weatherapp.ui.home.view.Utility
import com.example.weatherforecastapplication.MainActivity
import com.example.weatherforecastapplication.R

private const val CHANNEL_ID = 1
private const val FOREGROUND_ID = 2

class AlertService : Service() {

    lateinit var alertSharedPreferences: SharedPreferences
    lateinit var alertString: String
    var sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.packageName + "/" + R.raw.noti)
    private var notificationManager: NotificationManager? = null
    var alertWindowManger: AlertWindowManger? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        alertSharedPreferences =
            this.getSharedPreferences(Utility.ALERT, Context.MODE_PRIVATE)
        alertString = alertSharedPreferences.getString(Utility.ALERT, Utility.NOTIFICATION)!!
        // Get Data from Work Manager
        val description = intent?.getStringExtra("description")
        val event = intent?.getStringExtra("event")
        val countryName = intent?.getStringExtra("countryName")

        // Create Notification Channel
        notificationChannel()

        //start window manger
        if(alertString == Utility.ALARM){
            if (Settings.canDrawOverlays(this)) {
                alertWindowManger = description?.let { AlertWindowManger(this, it, event!! , countryName!!) }
                alertWindowManger!!.initializeWindowManager()
                startForeground(FOREGROUND_ID, makeNotification(event!! , countryName!!))
            }
        }
        else{
            startForeground(FOREGROUND_ID, makeNotification(event!!, countryName!!))
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun makeNotification(description: String , countryName : String): Notification {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        return NotificationCompat.Builder(applicationContext, "$CHANNEL_ID")
            .setSmallIcon(R.drawable.weather_icon)
            .setContentText(description)
            .setContentTitle("Weather Alarm")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$description at $countryName" )
            )
            .setSound(sound)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setAutoCancel(true)
            .build()
    }

    private fun notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: String = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("$CHANNEL_ID", name, importance)
            channel.enableVibration(true)
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            channel.setSound(sound, attributes)
            channel.description = description
            notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

}