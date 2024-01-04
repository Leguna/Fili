package com.arksana.fili.ui.settings

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import com.arksana.fili.R
import com.arksana.fili.databinding.FragmentSettingsBinding
import com.arksana.fili.ui.main.MainFragment
import com.arksana.fili.ui.settings.SettingsViewModel.Companion.CHANNEL_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment @Inject constructor() : PreferenceFragmentCompat() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        val switchPreference =
            findPreference<androidx.preference.SwitchPreferenceCompat>("daily_reminder")
        val feedBackPreference =
            findPreference<androidx.preference.Preference>("feedback")
        val languagePreference =
            findPreference<androidx.preference.Preference>("language")

        switchPreference?.setOnPreferenceChangeListener { _, newValue ->
            settingsViewModel.switchDailyReminder(newValue as Boolean)
            if (newValue)
                sendNotification()
            true
        }
        switchPreference?.isChecked = settingsViewModel.isDailyReminderActive()

        feedBackPreference?.setOnPreferenceClickListener {
            val browserIntent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.https_github_com_leguna_fili_issues))
                )
            startActivity(browserIntent)
            true
        }

        languagePreference?.setOnPreferenceClickListener {
            // Open language settings
            val intent = Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
            true
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun sendNotification() {
        createNotificationChannel()
        val intent = Intent(requireContext(), MainFragment::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val builder = requireContext().run {
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.daily_reminder_message))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
        }

        with(NotificationManagerCompat.from(requireContext())) {
            val notificationId = 1001
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
                return
            }
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Daily Reminder"
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(
                    requireContext(),
                    NotificationManager::class.java
                ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}