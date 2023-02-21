package hardcoder.dev.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class EnterWeightViewModel : ViewModel() {

    private val weight = MutableStateFlow(0)
    val state = weight.map {
        State(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(0)
    )

    fun updateWeight(selectedWeight: Int) {
        weight.value = selectedWeight
    }

    data class State(val weight: Int)
}