package com.zahra.space.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.QuranDao
import com.zahra.space.data.entity.QuranAyat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(private val quranDao: QuranDao) : ViewModel() {
    private val _currentAyat = MutableStateFlow<QuranAyat?>(null); val currentAyat: StateFlow<QuranAyat?> = _currentAyat
    private val _surahName = MutableStateFlow(""); val surahName: StateFlow<String> = _surahName
    private val _surahList = MutableStateFlow<List<QuranAyat>>(emptyList()); val surahList: StateFlow<List<QuranAyat>> = _surahList

    fun loadSurahList() = viewModelScope.launch { quranDao.getSurahList().collect { _surahList.value = it } }
    fun loadAyat(surahId: Int, verseId: Int) = viewModelScope.launch {
        quranDao.getAyat(surahId, verseId).collect { ayat ->
            _currentAyat.value = ayat
            _surahName.value = ayat?.surahName ?: ""
        }
    }
}
