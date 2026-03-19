package com.zahra.space.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()
    
    @TypeConverter
    fun fromString(value: String?): Map<String, Boolean>? {
        if (value == null) return null
        val type = object : TypeToken<Map<String, Boolean>>() {}.type
        return gson.fromJson(value, type)
    }
    
    @TypeConverter
    fun fromMap(map: Map<String, Boolean>?): String? {
        if (map == null) return null
        return gson.toJson(map)
    }
    
    @TypeConverter
    fun fromStringToList(value: String?): List<String>? {
        if (value == null) return null
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }
    
    @TypeConverter
    fun fromListToString(list: List<String>?): String? {
        if (list == null) return null
        return gson.toJson(list)
    }
}
