package com.example.minimum

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat

class Notification(private val context: Context) {
    private var notificationManager: NotificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private var builder: NotificationCompat.Builder? = null

    init {
        createNotificationChannel(context)
    }

    private fun createNotificationChannel(context: Context) {
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name: CharSequence = context.resources.getString(com.example.minimum.R.string.channel_name)
        val description: String = context.resources.getString(com.example.minimum.R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(channelId, name, importance)
        mChannel.description = description
        mChannel.enableLights(true)
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(true)
        mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        mNotificationManager.createNotificationChannel(mChannel)
    }

    fun createNotification(intent: Intent) {
        val pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        builder = NotificationCompat.Builder(context, channelId)
                .setContentIntent(pendingIntent)
                .setSmallIcon(com.example.minimum.R.drawable.ic_stat_name)
                .setContentTitle("Your daily read is ready")
                .setContentText("Don't miss out the new knowledge that awaits you!")
                .setAutoCancel(true)
    }

    fun sendNotification() {
        if (builder == null) {
            throw NoBuilderException("Nothing to notify, no notification was created")
        }
        notificationManager.notify(100, builder!!.build())
    }

    inner class NoBuilderException(message: String): Exception(message)

    companion object {
        const val channelId = "ARTICLE_NOTIFICATION"
    }
}