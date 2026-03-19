package com.zahra.space

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ZahraApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val prayerChannel = NotificationChannel("prayer", "Waktu Sholat", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Notifikasi waktu sholat"
                enableLights(true)
                enableVibration(true)
            }
            val gameChannel = NotificationChannel("game", "Notifikasi Game", NotificationManager.IMPORTANCE_LOW).apply {
                description = "Notifikasi dari dalam game"
                enableVibration(false)
            }
            val messageChannel = NotificationChannel("message", "Pesan F", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Pesan rahasia dari F"
            }
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(prayerChannel)
            nm.createNotificationChannel(gameChannel)
            nm.createNotificationChannel(messageChannel)
        }
    }
}

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        createNotificationChannels()
    }
