package hardcoder.dev.healther.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.repository.UserRepository
import hardcoder.dev.healther.ui.screens.welcome.Gender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    val userData = combine(
        userRepository.weight,
        userRepository.exerciseStressTime,
        userRepository.gender
    ) { weight, stressTime, gender ->
        UserState(
            weight,
            stressTime,
            gender,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = UserState(
            weight = 0,
            stressTime = 0,
            gender = Gender.MALE
        )
    )

    fun updateWeight(weight: Int) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.updateWeight(weight)
    }

    fun updateGender(gender: Gender) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.updateGender(gender)
    }

    fun updateExerciseStressTime(exerciseStressTime: Int) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.updateExerciseStressTime(exerciseStressTime)
    }

    class UserState(
        val weight: Int,
        val stressTime: Int,
        val gender: Gender
    )
}