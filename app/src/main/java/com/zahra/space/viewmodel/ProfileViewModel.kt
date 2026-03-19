package com.zahra.space.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {
    
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName
    
    private val _totalPoints = MutableStateFlow(0L)
    val totalPoints: StateFlow<Long> = _totalPoints
    
    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak
    
    private val _imanLevel = MutableStateFlow(50)
    val imanLevel: StateFlow<Int> = _imanLevel
    
    private val _joinDate = MutableStateFlow("")
    val joinDate: StateFlow<String> = _joinDate
    
    init {
        viewModelScope.launch {
            userDao.getUser().collect { user ->
                _userName.value = user.name.ifEmpty { "Zahra" }
                _totalPoints.value = user.totalPoints
                _streak.value = user.streak
                _imanLevel.value = user.imanLevel
                
                val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id"))
                _joinDate.value = dateFormat.format(Date(user.installDate))
            }
        }
    }
}
