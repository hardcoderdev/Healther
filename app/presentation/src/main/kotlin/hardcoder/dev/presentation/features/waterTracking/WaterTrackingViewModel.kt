package hardcoder.dev.presentation.features.waterTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.coroutines.mapItems
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.reward.currency.CurrencyCollector
import hardcoder.dev.logic.reward.currency.CurrencyProvider
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.waterTracking.resolvers.WaterPercentageResolver
import hardcoder.dev.logic.features.waterTracking.WaterTrackProvider
import hardcoder.dev.logic.features.waterTracking.WaterTrackingMillilitersDrunkProvider
import hardcoder.dev.logic.reward.experience.ExperienceCollector
import hardcoder.dev.math.safeDiv
import kotlinx.coroutines.flow.map

class WaterTrackingViewModel(
    currencyProvider: CurrencyProvider,
    waterTrackProvider: WaterTrackProvider,
    waterPercentageResolver: WaterPercentageResolver,
    millilitersDrunkProvider: WaterTrackingMillilitersDrunkProvider,
    dateTimeProvider: DateTimeProvider,
    currencyCollector: CurrencyCollector,
    experienceCollector: ExperienceCollector,
) : ViewModel() {

    val dailyRateProgressController = LoadingController<Float>(
        coroutineScope = viewModelScope,
        flow = millilitersDrunkProvider.provideMillilitersDrunkToDailyRateToday(
            dateRange = dateTimeProvider.currentDateRange(),
        ).map {
            it.millilitersDrunkCount safeDiv it.dailyWaterIntake
        }
    )

    val waterTracksLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = waterTrackProvider.provideWaterTracksByDayRange(
            dateTimeProvider.currentDateRange(),
        ).mapItems { waterTrack ->
            waterTrack.toItem(
                resolvedMillilitersCount = waterPercentageResolver.resolve(
                    drinkType = waterTrack.drinkType,
                    millilitersDrunk = waterTrack.millilitersCount,
                ),
            )
        },
    )

    val millilitersDrunkLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = millilitersDrunkProvider.provideMillilitersDrunkToDailyRateToday(
            dateRange = dateTimeProvider.currentDateRange(),
        ),
    )

    val collectRewardController = RequestController(
        coroutineScope = viewModelScope,
        canBeReset = true,
        request = {
            currencyCollector.collect(featureType = FeatureType.WATER_TRACKING)
            experienceCollector.collect(featureType = FeatureType.WATER_TRACKING)
        },
    )

    val rewardLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = currencyProvider.provideRewardsByDate(
            isCollected = false,
            featureType = FeatureType.WATER_TRACKING,
            dayRange = dateTimeProvider.currentDateRange(),
        ).map { rewardList ->
            rewardList.sumOf { reward ->
                reward.amount.toDouble()
            }
        },
    )
}