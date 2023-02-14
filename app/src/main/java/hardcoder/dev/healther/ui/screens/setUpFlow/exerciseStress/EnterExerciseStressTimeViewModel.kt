package hardcoder.dev.healther.ui.screens.setUpFlow.exerciseStress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EnterExerciseStressTimeViewModel(private val userRepository: UserRepository) : ViewModel() {

    val state = userRepository.exerciseStressTime.map {
        State(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            exerciseStressTime = 0
        )
    )

    fun updateExerciseStressTime(exerciseStressTime: Int) = viewModelScope.launch {
        userRepository.updateExerciseStressTime(exerciseStressTime)
    }

    data class State(val exerciseStressTime: Int)
}