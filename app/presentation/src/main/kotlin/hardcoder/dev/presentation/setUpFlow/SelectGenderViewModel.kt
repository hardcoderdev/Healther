package hardcoder.dev.presentation.setUpFlow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.logic.hero.gender.Gender
import hardcoder.dev.logic.hero.gender.GenderProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SelectGenderViewModel(genderProvider: GenderProvider) : ViewModel() {

    private val selectedGender = MutableStateFlow(Gender.MALE)
    private val availableGenders = genderProvider.provideAllGenders().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val state = combine(
        selectedGender,
        availableGenders
    ) { selectedGender, availableGenders ->
        State(
            selectedGender = selectedGender,
            availableGenderList = availableGenders
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            selectedGender = selectedGender.value,
            availableGenderList = availableGenders.value
        )
    )

    fun updateGender(newGender: Gender) {
        selectedGender.value = newGender
    }

    data class State(
        val selectedGender: Gender,
        val availableGenderList: List<Gender>
    )
}