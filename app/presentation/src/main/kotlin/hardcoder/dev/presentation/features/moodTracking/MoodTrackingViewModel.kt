package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.reward.currency.CurrencyCollector
import hardcoder.dev.logic.reward.currency.CurrencyProvider
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivitiesProvider
import hardcoder.dev.logic.reward.experience.ExperienceCollector
import kotlinx.coroutines.flow.map

class MoodTrackingViewModel(
    moodWithActivitiesProvider: MoodWithActivitiesProvider,
    dateTimeProvider: DateTimeProvider,
    currencyProvider: CurrencyProvider,
    currencyCollector: CurrencyCollector,
    experienceCollector: ExperienceCollector,
) : ViewModel() {

    val moodWithActivityLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = moodWithActivitiesProvider.provideMoodWithActivityList(
            dayRange = dateTimeProvider.currentDateRange(),
        ),
    )

    val collectRewardController = RequestController(
        coroutineScope = viewModelScope,
        canBeReset = true,
        request = {
            currencyCollector.collect(featureType = FeatureType.MOOD_TRACKING)
            experienceCollector.collect(featureType = FeatureType.MOOD_TRACKING)
        },
    )

    val rewardLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = currencyProvider.provideRewardsByDate(
            isCollected = false,
            featureType = FeatureType.MOOD_TRACKING,
            dayRange = dateTimeProvider.currentDateRange(),
        ).map { rewardList ->
            rewardList.sumOf { reward ->
                reward.amount.toDouble()
            }
        },
    )
}