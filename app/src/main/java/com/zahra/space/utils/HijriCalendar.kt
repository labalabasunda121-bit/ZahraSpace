package com.zahra.space.utils
import java.util.*
class HijriCalendar {
    data class HijriDate(val year: Int, val month: Int, val day: Int, val monthName: String)
    fun toHijri(gregorianDate: Date): HijriDate {
        val cal = Calendar.getInstance().apply { time = gregorianDate }
        val monthNames = arrayOf("Muharram","Safar","Rabiul Awwal","Rabiul Akhir","Jumadil Awwal","Jumadil Akhir","Rajab","Sya'ban","Ramadhan","Syawal","Dzulqa'dah","Dzulhijjah")
        // Sederhana: perkiraan kasar
        val year = cal.get(Calendar.YEAR) - 622
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        return HijriDate(year, month + 1, day, monthNames[month])
    }
}
