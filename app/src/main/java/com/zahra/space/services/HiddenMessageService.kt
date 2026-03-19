package com.zahra.space.services

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zahra.space.data.entity.HiddenMessage
import com.zahra.space.data.dao.HiddenMessageDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class HiddenMessageService(
    private val context: Context,
    private val hiddenMessageDao: HiddenMessageDao
) {
    
    suspend fun loadMessagesFromJson() {
        withContext(Dispatchers.IO) {
            try {
                val json = context.assets.open("data/hidden_messages.json").bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<HiddenMessage>>() {}.type
                val messages: List<HiddenMessage> = Gson().fromJson(json, type)
                hiddenMessageDao.insertAll(messages)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    suspend fun getRandomMessageByLocation(location: String): String? {
        return withContext(Dispatchers.IO) {
            hiddenMessageDao.getMessageByLocation(location).first()?.content
        }
    }
}
