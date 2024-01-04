package com.arksana.fili.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.arksana.fili.ui.settings.SettingsViewModel.Companion.DAILY_REMINDER_REQUEST_CODE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AlarmManagerModule {
    @Provides
    @Singleton
    fun provideAlarmManager(application: Application): AlarmManager {
        return application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
}

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        println("AlarmReceiver: onReceive")
        showNotification(context)
    }

    @SuppressLint("LaunchActivityFromNotification")
    private fun showNotification(context: Context?) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = getBroadcast(
            context,
            1001,
            intent,
            FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context!!, DAILY_REMINDER_REQUEST_CODE.toString())
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Daily Reminder")
            .setContentText("Don't forget to check your daily task!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(1001, builder.build())
    }

}