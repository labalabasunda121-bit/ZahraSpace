package com.zahra.space.utils
import java.util.*
class PrayerTimesCalculator {
    data class PrayerTimes(val subuh: Long, val dzuhur: Long, val ashar: Long, val maghrib: Long, val isya: Long)
    fun calculatePrayerTimes(date: Date, latitude: Double, longitude: Double): PrayerTimes {
        val cal = Calendar.getInstance().apply { time = date }
        cal.set(Calendar.HOUR_OF_DAY, 4); cal.set(Calendar.MINUTE, 30); val subuh = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 12); cal.set(Calendar.MINUTE, 0); val dzuhur = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 15); cal.set(Calendar.MINUTE, 15); val ashar = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 18); cal.set(Calendar.MINUTE, 0); val maghrib = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 19); cal.set(Calendar.MINUTE, 15); val isya = cal.timeInMillis
        return PrayerTimes(subuh, dzuhur, ashar, maghrib, isya)
    }
}
