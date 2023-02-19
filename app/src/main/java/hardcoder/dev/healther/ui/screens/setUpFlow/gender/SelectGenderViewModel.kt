package hardcoder.dev.healther.ui.screens.setUpFlow.gender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.repository.Gender
import hardcoder.dev.healther.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SelectGenderViewModel(private val userRepository: UserRepository) : ViewModel() {

    val state = userRepository.gender.map {
        State(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            Gender.MALE
        )
    )

    fun updateGender(gender: Gender) {
        viewModelScope.launch {
            userRepository.updateGender(gender)
        }
    }

    data class State(val gender: Gender)
}