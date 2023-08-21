package hardcoder.dev.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toLocalDateTime
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import hardcoder.dev.logic.appPreferences.AppPreferenceUpdater
import hardcoder.dev.logic.reward.penalty.PenaltyManager
import hardcoder.dev.logic.hero.HeroProvider
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

class SplashViewModel(
    appPreferenceProvider: AppPreferenceProvider,
    heroProvider: HeroProvider,
    private val appPreferenceUpdater: AppPreferenceUpdater,
    private val penaltyManager: PenaltyManager,
    private val dateTimeProvider: DateTimeProvider,
) : ViewModel() {

    private val appPreference = appPreferenceProvider.provideAppPreference()
    private val isFirstLaunch = appPreference.map { it == null }
    private val lastEntranceDate = appPreference.mapNotNull { it?.lastEntranceDateTime?.toLocalDateTime() }

    val isFirstLaunchLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = isFirstLaunch,
    )

    val healthPointsLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = heroProvider.provideHero().map {
            it?.currentHealthPoints ?: 0
        },
    )
}