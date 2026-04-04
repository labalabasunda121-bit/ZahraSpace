package com.zahra.space

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.room.Room
import com.zahra.space.data.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ZahraApplication : Application() {
    
    companion object {
        lateinit var database: AppDatabase
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        
        // Inisialisasi database di awal
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "zahra_space_database"
        )
        .createFromAsset("database/quran.sql")
        .fallbackToDestructiveMigration()
        .build()
        
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val prayerChannel = NotificationChannel(
                "prayer", 
                "Waktu Sholat", 
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi waktu sholat"
                enableLights(true)
                enableVibration(true)
            }
            
            val gameChannel = NotificationChannel(
                "game", 
                "Notifikasi Game", 
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notifikasi dari dalam game"
                enableVibration(false)
            }
            
            val messageChannel = NotificationChannel(
                "message", 
                "Pesan F", 
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Pesan rahasia dari F"
            }
            
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(prayerChannel)
            nm.createNotificationChannel(gameChannel)
            nm.createNotificationChannel(messageChannel)
        }
    }
}
