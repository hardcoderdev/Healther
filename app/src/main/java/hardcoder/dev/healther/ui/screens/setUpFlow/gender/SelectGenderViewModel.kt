package hardcoder.dev.healther.ui.screens.setUpFlow.gender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.entities.Gender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SelectGenderViewModel : ViewModel() {

    private val gender = MutableStateFlow(Gender.MALE)
    val state = gender.map {
        State(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(Gender.MALE)
    )

    fun updateGender(selectedGender: Gender) {
        gender.value = selectedGender
    }

    data class State(val gender: Gender)
}