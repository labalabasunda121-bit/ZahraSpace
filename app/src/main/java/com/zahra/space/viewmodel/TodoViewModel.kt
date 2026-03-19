package com.zahra.space.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.TodoDao
import com.zahra.space.data.entity.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoDao: TodoDao
) : ViewModel() {
    
    private val _activeTodos = MutableStateFlow<List<Todo>>(emptyList())
    val activeTodos: StateFlow<List<Todo>> = _activeTodos
    
    fun loadActiveTodos() {
        viewModelScope.launch {
            todoDao.getActiveTodos().collect { list ->
                _activeTodos.value = list
            }
        }
    }
}
