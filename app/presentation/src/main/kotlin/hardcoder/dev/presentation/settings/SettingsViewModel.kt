package hardcoder.dev.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logics.appPreferences.AppPreferenceProvider
import kotlinx.coroutines.flow.filterNotNull

class SettingsViewModel(
    appPreferenceProvider: AppPreferenceProvider,
) : ViewModel() {

    val preferencesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = appPreferenceProvider.provideAppPreference()
            .filterNotNull(),
    )
}