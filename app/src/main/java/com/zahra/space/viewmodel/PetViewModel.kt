package com.zahra.space.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.PetDao
import com.zahra.space.data.entity.Pet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PetUiState(val hunger: Int = 50, val happiness: Int = 50, val cleanliness: Int = 50)

@HiltViewModel
class PetViewModel @Inject constructor(private val petDao: PetDao) : ViewModel() {
    private val _petState = MutableStateFlow(PetUiState())
    val petState: StateFlow<PetUiState> = _petState
    init {
        viewModelScope.launch {
            petDao.getPets().collect { pets ->
                if (pets.isNotEmpty()) {
                    val p = pets.first()
                    _petState.value = PetUiState(p.hunger, p.happiness, p.cleanliness)
                }
            }
        }
    }
    fun feed() = viewModelScope.launch { petDao.getPets().collect { pets -> if (pets.isNotEmpty()) petDao.decreaseHunger(pets.first().id, 20) } }
    fun play() = viewModelScope.launch { petDao.getPets().collect { pets -> if (pets.isNotEmpty()) petDao.increaseHappiness(pets.first().id, 20) } }
    fun clean() = viewModelScope.launch { petDao.getPets().collect { pets -> if (pets.isNotEmpty()) petDao.cleanPet(pets.first().id) } }
}
