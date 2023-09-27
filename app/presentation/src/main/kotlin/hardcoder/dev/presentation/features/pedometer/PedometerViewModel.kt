package hardcoder.dev.presentation.features.pedometer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toLocalDateTime
import hardcoder.dev.logic.reward.currency.CurrencyCollector
import hardcoder.dev.logic.reward.currency.CurrencyProvider
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.pedometer.PedometerDailyRateStepsProvider
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatisticProvider
import hardcoder.dev.logic.reward.experience.ExperienceCollector
import hardcoder.dev.math.safeDiv
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class PedometerViewModel(
    pedometerManager: PedometerManager,
    pedometerTrackProvider: PedometerTrackProvider,
    pedometerStatisticProvider: PedometerStatisticProvider,
    pedometerDailyRateStepsProvider: PedometerDailyRateStepsProvider,
    dateTimeProvider: DateTimeProvider,
    currencyProvider: CurrencyProvider,
    currencyCollector: CurrencyCollector,
    experienceCollector: ExperienceCollector,
) : ViewModel() {

    val dailyRateProgressController = LoadingController<Float>(
        coroutineScope = viewModelScope,
        flow = combine(
            pedometerStatisticProvider.providePedometerStatistic(
                range = dateTimeProvider.currentDateRange(),
            ),
            pedometerDailyRateStepsProvider.resolve(),
        ) { pedometerStatistics, dailyRateInSteps ->
            pedometerStatistics.totalSteps safeDiv dailyRateInSteps
        },
    )

    val dailyRateStepsLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = pedometerDailyRateStepsProvider.resolve(),
    )

    val statisticLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = pedometerStatisticProvider.providePedometerStatistic(),
    )

    val todayStatisticLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = pedometerStatisticProvider.providePedometerStatistic(
            range = dateTimeProvider.currentDateRange(),
        ),
    )

    val chartEntriesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = pedometerTrackProvider.providePedometerTracksByRange(
            dateTimeProvider.currentDateRange(),
        ).map { pedometerTracks ->
            PedometerChartData(
                entriesList = pedometerTracks.groupBy {
                    it.range.start.toLocalDateTime().hour
                }.map { entry ->
                    PedometerChartEntry(
                        from = entry.key,
                        to = entry.value.sumOf { it.stepsCount },
                    )
                },
            )
        },
    )

    val pedometerAvailabilityLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = pedometerManager.availability,
    )

    val pedometerToggleController = ToggleController(
        coroutineScope = viewModelScope,
        isActiveFlow = pedometerManager.isTracking,
        toggle = {
            pedometerManager.requestBattery()
            pedometerManager.requestPermissions()
            pedometerManager.toggleTracking()
        },
    )

    val collectRewardController = RequestController(
        coroutineScope = viewModelScope,
        canBeReset = true,
        request = {
            currencyCollector.collect(featureType = FeatureType.PEDOMETER)
            experienceCollector.collect(featureType = FeatureType.PEDOMETER)
        },
    )

    val rewardLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = currencyProvider.provideRewardsByDate(
            isCollected = false,
            featureType = FeatureType.PEDOMETER,
            dayRange = dateTimeProvider.currentDateRange(),
        ).map { rewardList ->
            rewardList.sumOf { reward ->
                reward.amount.toDouble()
            }
        },
    )
}