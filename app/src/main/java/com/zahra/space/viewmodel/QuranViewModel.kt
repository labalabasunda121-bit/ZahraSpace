package com.zahra.space.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zahra.space.data.dao.QuranDao
import com.zahra.space.data.entity.QuranAyat
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SurahInfo(val latin: String, val arabic: String)

@HiltViewModel
class QuranViewModel @Inject constructor(
    private val quranDao: QuranDao,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _currentAyat = MutableStateFlow<QuranAyat?>(null)
    val currentAyat: StateFlow<QuranAyat?> = _currentAyat

    private val _currentSurahName = MutableStateFlow("")
    val currentSurahName: StateFlow<String> = _currentSurahName

    private val _surahList = MutableStateFlow<List<QuranAyat>>(emptyList())
    val surahList: StateFlow<List<QuranAyat>> = _surahList

    private val _surahNames = MutableStateFlow<Map<Int, SurahInfo>>(emptyMap())
    val surahNames: StateFlow<Map<Int, SurahInfo>> = _surahNames

    init {
        loadSurahNames()
    }

    fun loadSurahNames() {
        viewModelScope.launch {
            try {
                val json = context.assets.open("data/surah_names.json").bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<Map<String, Any>>>() {}.type
                val list: List<Map<String, Any>> = Gson().fromJson(json, type)
                val map = mutableMapOf<Int, SurahInfo>()
                list.forEach { item ->
                    val id = (item["id"] as Double).toInt()
                    val latin = item["latin"] as String
                    val arabic = item["arabic"] as String
                    map[id] = SurahInfo(latin, arabic)
                }
                _surahNames.value = map
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadSurahList() {
        viewModelScope.launch {
            quranDao.getSurahList().collect { list ->
                _surahList.value = list
            }
        }
    }

    fun loadAyat(surahId: Int, verseId: Int) {
        viewModelScope.launch {
            quranDao.getAyat(surahId, verseId).collect { ayat ->
                _currentAyat.value = ayat
                val name = _surahNames.value[surahId]
                _currentSurahName.value = name?.latin ?: "Surah $surahId"
            }
        }
    }
}
