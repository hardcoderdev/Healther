package hardcoder.dev.healther.ui

import android.util.Log
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
        userRepository.gender,
        userRepository.isFirstLaunch
    ) { weight, stressTime, gender, isFirstLaunch ->
        Log.d("dwdw", "$weight $stressTime ${gender.name} $isFirstLaunch")
        FetchingState.Loaded(
            UserState(
                weight,
                stressTime,
                gender,
                isFirstLaunch
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = FetchingState.Loading()
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

    fun updateFirstLaunch() = viewModelScope.launch(Dispatchers.IO) {
        userRepository.updateFirstLaunch(isFirstLaunch = false)
    }

    class UserState(
        val weight: Int,
        val stressTime: Int,
        val gender: Gender,
        val isFirstLaunch: Boolean
    )

    sealed class FetchingState {
        class Loading() : FetchingState()
        class Loaded(val userState: UserState) : FetchingState()
    }
}