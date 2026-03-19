package com.zahra.space.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.UserDao
import com.zahra.space.data.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {
    
    private val _isOnboardingComplete = MutableStateFlow(false)
    val isOnboardingComplete: StateFlow<Boolean> = _isOnboardingComplete
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun saveUser(user: User) {
        viewModelScope.launch {
            try {
                // Hapus user lama kalo ada
                runCatching { userDao.deleteAll() }.exceptionOrNull()
                
                // Insert user baru
                userDao.insert(user)
                _isOnboardingComplete.value = true
                _error.value = null
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Gagal menyimpan data: ${e.message}"
            }
        }
    }
}
