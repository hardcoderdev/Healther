package hardcoder.dev.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.in_app_review.ReviewManager
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import hardcoder.dev.logic.appPreferences.AppPreferenceUpdater
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.datetime.Clock

class SettingsViewModel(
    reviewManager: ReviewManager,
    appPreferenceUpdater: AppPreferenceUpdater,
    appPreferenceProvider: AppPreferenceProvider
) : ViewModel() {

    val preferencesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = appPreferenceProvider.provideAppPreference().filterNotNull()
    )

    val appReviewRequestController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            require(reviewManager.launchReviewFlow())

            val preferencesState = preferencesLoadingController.state.value
            require(preferencesState is LoadingController.State.Loaded)

            appPreferenceUpdater.update(
                preferencesState.data.copy(
                    lastAppReviewRequestTime = Clock.System.now()
                )
            )
        }
    )
}