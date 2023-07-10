package hardcoder.dev.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.request.SingleRequestController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.inAppReview.ReviewManager
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import hardcoder.dev.logic.appPreferences.AppPreferenceUpdater
import kotlinx.coroutines.flow.filterNotNull

class SettingsViewModel(
    reviewManager: ReviewManager,
    appPreferenceUpdater: AppPreferenceUpdater,
    appPreferenceProvider: AppPreferenceProvider,
    dateTimeProvider: DateTimeProvider,
) : ViewModel() {

    val preferencesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = appPreferenceProvider.provideAppPreference().filterNotNull(),
    )

    val appReviewRequestController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            require(reviewManager.launchReviewFlow())

            val preferencesState = preferencesLoadingController.state.value
            require(preferencesState is LoadingController.State.Loaded)

            appPreferenceUpdater.update(
                preferencesState.data.copy(
                    lastAppReviewRequestTime = dateTimeProvider.currentInstant(),
                ),
            )
        },
    )
}