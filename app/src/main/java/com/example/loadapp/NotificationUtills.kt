package com.example.loadapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationUtills {

    fun createChannel(
        channelId: String,
        channelName: String,
        notificationManager: NotificationManager
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = R.string.notification_description.toString()
            notificationChannel.apply {
                setShowBadge(false)
            }

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun buildNotification(
        application: MainActivity,
        messageBody: String,
        fileUrl: String,
        fileName: String,
        fileStatus:String
    ) {
        val contentIntent = Intent(application, MainActivity::class.java)
        val contentPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(application, 0, contentIntent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(
                application,
                0,
                contentIntent,
                PendingIntent.FLAG_ONE_SHOT
            )
        }

        val detailsIntent = Intent(application, DetailActivity::class.java)
        detailsIntent.putExtra(DetailActivity.FILE_URL, fileUrl)
        detailsIntent.putExtra(DetailActivity.FILE_NAME, fileName)
        detailsIntent.putExtra(DetailActivity.FILE_STATUS, fileStatus)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(application, 0, detailsIntent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(
                application,
                0,
                detailsIntent,
                PendingIntent.FLAG_ONE_SHOT
            )
        }
        val builder = NotificationCompat.Builder(
            application,
            MainActivity.CHANNEL_ID
        )

        val notificationImage = BitmapFactory.decodeResource(
            application.resources,
            R.drawable.ic_launcher_foreground
        )
        val bigPicStyle =
            NotificationCompat.BigPictureStyle().bigPicture(notificationImage).bigLargeIcon(null)

        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(application.getString(R.string.notification_title))
            .setContentIntent(contentPendingIntent)
            .setContentText(messageBody)
            .setStyle(bigPicStyle)
            .setLargeIcon(notificationImage)
            .addAction(
                R.drawable.ic_launcher_foreground,
                "Open details",
                pendingIntent
            ).setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(application)
        notificationManager.notify(1001, builder.build())
    }
}