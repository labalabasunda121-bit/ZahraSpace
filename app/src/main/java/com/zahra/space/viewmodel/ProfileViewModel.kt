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
class ProfileViewModel @Inject constructor(private val userDao: UserDao) : ViewModel() {
    private val _userName = MutableStateFlow(""); val userName: StateFlow<String> = _userName
    private val _totalPoints = MutableStateFlow(0L); val totalPoints: StateFlow<Long> = _totalPoints
    private val _imanLevel = MutableStateFlow(0); val imanLevel: StateFlow<Int> = _imanLevel
    private val _joinDate = MutableStateFlow(""); val joinDate: StateFlow<String> = _joinDate
    init {
        viewModelScope.launch {
            userDao.getUser().collect { user ->
                _userName.value = user.name
                _totalPoints.value = user.totalPoints
                _imanLevel.value = user.imanLevel
                _joinDate.value = SimpleDateFormat("dd MMM yyyy", Locale("id")).format(Date(user.installDate))
            }
        }
    }
}
