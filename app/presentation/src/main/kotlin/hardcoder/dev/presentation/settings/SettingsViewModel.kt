package hardcoder.dev.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import hardcoder.dev.logic.appPreferences.AppPreferenceUpdater
import hardcoder.dev.in_app_review.ReviewManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class SettingsViewModel(
    private val reviewManager: ReviewManager,
    private val appPreferenceUpdater: AppPreferenceUpdater,
    appPreferenceProvider: AppPreferenceProvider
) : ViewModel() {

    private val appPreferences = appPreferenceProvider.provideAppPreference().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    val state = appPreferences.filterNotNull().map { appPreferences ->
        State(
            isAppAlreadyRated = appPreferences.lastAppReviewRequestTime != null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            isAppAlreadyRated = false
        )
    )

    fun launchInAppReviewFlow() {
        viewModelScope.launch {
            val isSuccessful = reviewManager.launchReviewFlow()
            if (isSuccessful) {
                appPreferenceUpdater.update(
                    requireNotNull(appPreferences.value).copy(
                        lastAppReviewRequestTime = Clock.System.now()
                    )
                )
            }
        }
    }

    data class State(val isAppAlreadyRated: Boolean)
}