package hardcoder.dev.healther.ui.screens.setUpFlow.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EnterWeightViewModel(private val userRepository: UserRepository) : ViewModel() {

    val state = userRepository.weight.map {
        State(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            weight = 0
        )
    )

    fun updateWeight(weight: Int) {
        viewModelScope.launch {
            userRepository.updateWeight(weight)
        }
    }

    data class State(val weight: Int)
}