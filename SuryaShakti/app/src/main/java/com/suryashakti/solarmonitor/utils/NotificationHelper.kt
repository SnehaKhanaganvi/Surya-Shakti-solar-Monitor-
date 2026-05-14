package com.suryashakti.solarmonitor.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.suryashakti.solarmonitor.R

object NotificationHelper {

    private const val CHANNEL_ID   = "surya_shakti_channel"
    private const val CHANNEL_NAME = "Surya-Shakti Alerts"
    private const val NOTIF_PEAK   = 1001
    private const val NOTIF_SAVING = 1002

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Solar energy alerts and tips" }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun sendPeakSunAlert(context: Context, tipText: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_sun)
            .setContentTitle("☀️ High Sun — Peak Solar Time!")
            .setContentText(tipText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(tipText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setColor(0xFFFFD600.toInt())
        try {
            NotificationManagerCompat.from(context).notify(NOTIF_PEAK, builder.build())
        } catch (e: SecurityException) { /* permission not granted */ }
    }

    fun sendSavingsAlert(context: Context, savedAmount: Float, date: String) {
        val text = "You saved ₹%.2f on solar energy today! Great job, Prosumer!".format(savedAmount)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_sun)
            .setContentTitle("💰 Daily Savings — $date")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .setColor(0xFF00E676.toInt())
        try {
            NotificationManagerCompat.from(context).notify(NOTIF_SAVING, builder.build())
        } catch (e: SecurityException) { /* permission not granted */ }
    }
}
