package hardcoder.dev.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SplashViewModel(appPreferenceProvider: AppPreferenceProvider) : ViewModel() {

    val state = appPreferenceProvider.provideAppPreference().map { appPreferences ->
        FetchingState.Loaded(State(isFirstLaunch = appPreferences?.let {
            false
        } ?: run {
            LAUNCH_FIRST
        }))
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

    companion object {
        const val LAUNCH_FIRST = true
    }
}