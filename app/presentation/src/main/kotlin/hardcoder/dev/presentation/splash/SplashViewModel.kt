package hardcoder.dev.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import kotlinx.coroutines.flow.map

class SplashViewModel(
    appPreferenceProvider: AppPreferenceProvider,
) : ViewModel() {

    private val appPreference = appPreferenceProvider.provideAppPreference()
    private val isFirstLaunch = appPreference.map { it == null }

    val isFirstLaunchLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = isFirstLaunch,
    )
}