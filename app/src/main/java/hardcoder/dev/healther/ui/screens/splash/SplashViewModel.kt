package hardcoder.dev.healther.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SplashViewModel(
    userRepository: UserRepository
) : ViewModel() {

    val state = userRepository.isFirstLaunch.map {
        FetchingState.Loaded(State(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = FetchingState.Loading
    )

    data class State(val isFirstLaunch: Boolean)

    sealed class FetchingState {
        data class Loaded(val state: State) : FetchingState()
        object Loading : FetchingState()
    }
}