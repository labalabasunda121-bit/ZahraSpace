package com.zahra.space.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.UserDao
import com.zahra.space.data.dao.PetDao
import com.zahra.space.data.dao.DailyChecklistDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class PetState(
    val level: Int = 1,
    val hunger: Int = 50,
    val happiness: Int = 50,
    val cleanliness: Int = 50,
    val energy: Int = 80
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userDao: UserDao,
    private val petDao: PetDao,
    private val checklistDao: DailyChecklistDao
) : ViewModel() {

    private val _userName = MutableStateFlow("Zahra")
    val userName: StateFlow<String> = _userName

    private val _totalPoints = MutableStateFlow(0L)
    val totalPoints: StateFlow<Long> = _totalPoints

    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak

    private val _imanLevel = MutableStateFlow(50)
    val imanLevel: StateFlow<Int> = _imanLevel

    private val _petStatus = MutableStateFlow(PetState())
    val petStatus: StateFlow<PetState> = _petStatus

    private val _greeting = MutableStateFlow("")
    val greeting: StateFlow<String> = _greeting

    private val _currentDate = MutableStateFlow("")
    val currentDate: StateFlow<String> = _currentDate

    init {
        loadUserData()
        updateGreeting()
        updateCurrentDate()
        loadPetStatus()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            userDao.getUser().collect { user ->
                _userName.value = user.name.ifEmpty { "Zahra" }
                _totalPoints.value = user.totalPoints
                _streak.value = user.streak
                _imanLevel.value = user.imanLevel
            }
        }
    }

    private fun updateGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        _greeting.value = when (hour) {
            in 0..10 -> "Selamat Pagi"
            in 11..14 -> "Selamat Siang"
            in 15..17 -> "Selamat Sore"
            else -> "Selamat Malam"
        }
    }

    private fun updateCurrentDate() {
        _currentDate.value = SimpleDateFormat("dd MMMM yyyy", Locale("id")).format(Date())
    }

    private fun loadPetStatus() {
        viewModelScope.launch {
            petDao.getPets().collect { pets ->
                if (pets.isNotEmpty()) {
                    val p = pets.first()
                    _petStatus.value = PetState(
                        level = p.level,
                        hunger = p.hunger,
                        happiness = p.happiness,
                        cleanliness = p.cleanliness,
                        energy = p.energy
                    )
                }
            }
        }
    }

    fun feedPet() {
        viewModelScope.launch {
            petDao.getPets().collect { pets ->
                if (pets.isNotEmpty()) {
                    petDao.decreaseHunger(pets.first().id, 20)
                    loadPetStatus()
                }
            }
        }
    }

    fun playWithPet() {
        viewModelScope.launch {
            petDao.getPets().collect { pets ->
                if (pets.isNotEmpty()) {
                    petDao.increaseHappiness(pets.first().id, 20)
                    loadPetStatus()
                }
            }
        }
    }

    fun cleanPet() {
        viewModelScope.launch {
            petDao.getPets().collect { pets ->
                if (pets.isNotEmpty()) {
                    petDao.cleanPet(pets.first().id)
                    loadPetStatus()
                }
            }
        }
    }
    
    fun sleepPet() {
        viewModelScope.launch {
            petDao.getPets().collect { pets ->
                if (pets.isNotEmpty()) {
                    // Implementasi tidur
                    loadPetStatus()
                }
            }
        }
    }
}
