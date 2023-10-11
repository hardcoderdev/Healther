package hardcoder.dev.presentation.splash

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logics.appPreferences.AppPreferenceProvider
import kotlinx.coroutines.flow.map

class SplashViewModel(
    appPreferenceProvider: AppPreferenceProvider,
) : ScreenModel {

    private val appPreference = appPreferenceProvider.provideAppPreference()
    private val isFirstLaunch = appPreference.map { it == null }

    val isFirstLaunchLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = isFirstLaunch,
    )
}