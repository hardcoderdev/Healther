package hardcoder.dev.presentation.dashboard.settings

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.review.ReviewManager
import hardcoder.dev.in_app_review.requestReviewFlowAsync
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import hardcoder.dev.logic.appPreferences.AppPreferenceUpdater
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val reviewManager: ReviewManager,
    private val appPreferenceUpdater: AppPreferenceUpdater,
    appPreferenceProvider: AppPreferenceProvider,
) : ViewModel() {

    private val appPreferences = appPreferenceProvider.provideAppPreference().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    val state = appPreferences.filterNotNull().map { appPreferences ->
        State(isAppAlreadyRated = appPreferences.isAppAlreadyRated)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            isAppAlreadyRated = false
        )
    )

    fun launchInAppReviewFlow(currentActivity: Activity) {
        viewModelScope.launch {
            val isSuccessful = reviewManager.requestReviewFlowAsync(currentActivity)
            if (isSuccessful) {
                appPreferenceUpdater.update(
                    requireNotNull(appPreferences.value).copy(
                        isAppAlreadyRated = true
                    )
                )
            }
        }
    }

    data class State(val isAppAlreadyRated: Boolean)
}