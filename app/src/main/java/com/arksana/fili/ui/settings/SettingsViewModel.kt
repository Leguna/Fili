package com.arksana.fili.ui.settings

import android.Manifest
import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings.Global.getString
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import com.arksana.fili.R
import com.arksana.fili.services.AlarmReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val application: Application,
    private val alarmManager: AlarmManager,
) : ViewModel() {

    companion object {
        const val DAILY_REMINDER_KEY = "daily_reminder"
        const val DAILY_REMINDER_REQUEST_CODE = 1001
        const val CHANNEL_ID = "daily_reminder_channel"
    }

    fun switchDailyReminder(isActive: Boolean) {
        val sharedPreferences =
            application.getSharedPreferences(DAILY_REMINDER_KEY, Application.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(DAILY_REMINDER_KEY, isActive)
        editor.apply()

        if (isActive) {
            scheduleDailyReminder()
        } else {
            cancelDailyReminder()
        }
    }

    private fun cancelDailyReminder() {
        val intent = Intent(application, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            application,
            DAILY_REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }

    private fun scheduleDailyReminder() {
        val intent = Intent(application, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            application,
            DAILY_REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun isDailyReminderActive(): Boolean {
        val sharedPreferences =
            application.getSharedPreferences(DAILY_REMINDER_KEY, Application.MODE_PRIVATE)
        return sharedPreferences.getBoolean(DAILY_REMINDER_KEY, false)
    }

}