package hardcoder.dev.presentation.splash

import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logics.appPreferences.AppPreferenceProvider
import hardcoder.dev.viewmodel.ViewModel
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