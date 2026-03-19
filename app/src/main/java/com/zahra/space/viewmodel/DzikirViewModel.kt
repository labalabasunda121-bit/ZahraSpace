package com.zahra.space.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.DzikirDao
import com.zahra.space.data.entity.Dzikir
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DzikirViewModel @Inject constructor(
    private val dzikirDao: DzikirDao
) : ViewModel() {
    
    private val _dzikirList = MutableStateFlow<List<Dzikir>>(emptyList())
    val dzikirList: StateFlow<List<Dzikir>> = _dzikirList
    
    fun loadDzikir() {
        viewModelScope.launch {
            dzikirDao.getDzikirByCategory("all").collect { list ->
                _dzikirList.value = list
            }
        }
    }
}
