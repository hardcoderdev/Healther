package hardcoder.dev.presentation.settings

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logics.appPreferences.AppPreferenceProvider
import kotlinx.coroutines.flow.filterNotNull

class SettingsViewModel(
    appPreferenceProvider: AppPreferenceProvider,
) : ScreenModel {

    val preferencesLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = appPreferenceProvider.provideAppPreference()
            .filterNotNull(),
    )
}