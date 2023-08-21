package hardcoder.dev.presentation.dailyStreak

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DailyStreakCheckerViewModel(
    appPreferenceProvider: AppPreferenceProvider,
) : ViewModel() {

    private val lastEntranceDate = appPreferenceProvider.provideAppPreference()
        .map {
            requireNotNull(it).lastEntranceDateTime
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0,
        )
}