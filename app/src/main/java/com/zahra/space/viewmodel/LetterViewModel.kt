package com.zahra.space.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.MonthlyLetterDao
import com.zahra.space.data.entity.MonthlyLetter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LetterViewModel @Inject constructor(private val letterDao: MonthlyLetterDao) : ViewModel() {
    private val _letters = MutableStateFlow<List<MonthlyLetter>>(emptyList())
    val letters: StateFlow<List<MonthlyLetter>> = _letters
    fun loadLetters() = viewModelScope.launch {
        letterDao.getAllLetters().collect { _letters.value = it }
    }
}
