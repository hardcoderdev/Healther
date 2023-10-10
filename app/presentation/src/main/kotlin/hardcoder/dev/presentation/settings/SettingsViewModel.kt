package hardcoder.dev.presentation.settings

import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logics.appPreferences.AppPreferenceProvider
import hardcoder.dev.viewmodel.ViewModel
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