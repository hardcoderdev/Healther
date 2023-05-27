package hardcoder.dev.presentation.setUpFlow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import kotlinx.coroutines.flow.map

class SplashViewModel(appPreferenceProvider: AppPreferenceProvider) : ViewModel() {
    val isFirstLaunchLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = appPreferenceProvider.provideAppPreference().map { it == null }
    )
}