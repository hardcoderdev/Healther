package hardcoder.dev.healther.ui.screens.setUpFlow.exerciseStress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class EnterExerciseStressTimeViewModel : ViewModel() {

    val exerciseStressTime = MutableStateFlow(0)
    val state = exerciseStressTime.map {
        State(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            exerciseStressTime = 0
        )
    )

    fun updateExerciseStressTime(selectedExerciseStressTime: Int) {
        exerciseStressTime.value = selectedExerciseStressTime
    }

    data class State(val exerciseStressTime: Int)
}