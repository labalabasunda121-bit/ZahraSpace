package com.zahra.space.data
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class Converters {
    private val gson = Gson()
    @TypeConverter fun fromString(value: String?): Map<String, Boolean>? = value?.let { gson.fromJson(it, object : TypeToken<Map<String, Boolean>>() {}.type) }
    @TypeConverter fun fromMap(map: Map<String, Boolean>?): String? = gson.toJson(map)
    @TypeConverter fun fromStringToList(value: String?): List<String>? = value?.let { gson.fromJson(it, object : TypeToken<List<String>>() {}.type) }
    @TypeConverter fun fromListToString(list: List<String>?): String? = gson.toJson(list)
}
