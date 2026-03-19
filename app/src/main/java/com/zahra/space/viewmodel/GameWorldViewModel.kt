package com.zahra.space.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.HiddenMessageDao
import com.zahra.space.data.dao.RestaurantDao
import com.zahra.space.data.dao.UserDao
import com.zahra.space.game.RestaurantState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameWorldViewModel @Inject constructor(
    private val userDao: UserDao,
    private val hiddenMessageDao: HiddenMessageDao,
    private val restaurantDao: RestaurantDao
) : ViewModel() {

    private val _balance = MutableStateFlow(0)
    val balance: StateFlow<Int> = _balance

    private val _restaurant = MutableStateFlow(RestaurantState())
    val restaurant: StateFlow<RestaurantState> = _restaurant

    init {
        loadUserBalance()
    }

    private fun loadUserBalance() {
        viewModelScope.launch {
            userDao.getUser().collect { user ->
                _balance.value = user.totalPoints.toInt()
            }
        }
    }

    fun findHiddenMessage(location: String) {
        viewModelScope.launch {
            // Perbaikan: panggil suspend function dengan benar
            val message = hiddenMessageDao.getMessageByLocation(location).first()
            message?.let {
                hiddenMessageDao.update(it.copy(isFound = true))
                userDao.addPoints(it.pointsReward)
                _balance.value += it.pointsReward
            }
        }
    }
}
