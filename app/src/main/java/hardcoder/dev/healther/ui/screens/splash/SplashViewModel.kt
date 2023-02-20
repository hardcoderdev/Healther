package hardcoder.dev.healther.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.logic.providers.AppPreferenceProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SplashViewModel(appPreferenceProvider: AppPreferenceProvider) : ViewModel() {

    val state = appPreferenceProvider.provideAppPreference().map {
        FetchingState.Loaded(State(it?.isFirstLaunch ?: LAUNCH_FIRST))
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

    companion object{
        const val LAUNCH_FIRST = true
    }
}