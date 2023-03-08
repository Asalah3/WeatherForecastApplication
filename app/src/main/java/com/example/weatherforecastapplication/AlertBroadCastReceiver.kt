package com.example.weatherforecastapplication

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews

class AlertBroadCastReceiver : BroadcastReceiver() {





    override fun onReceive(context: Context, intent: Intent) {
        notification(context)
    }
}
//TODO
// register Broadcast
// Intent Filter
@SuppressLint("RemoteViewLayout")
private fun notification(context: Context){
     val channelId = "i.apps.notifications"
     val description = "Test notification"
    var builder: Notification.Builder

    var notificationManager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val contentView = RemoteViews(context.packageName, R.layout.fragment_alerts)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        var notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)

        builder = Notification.Builder(context, channelId)
            .setContent(contentView)
            .setSmallIcon(R.drawable.weather_icon)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.weather_icon))
    } else {

        builder = Notification.Builder(context)
            .setContent(contentView)
            .setSmallIcon(R.drawable.weather_icon)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.weather_icon))
    }
    notificationManager.notify(1234, builder.build())
}