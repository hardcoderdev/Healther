package hardcoder.dev.presentation.features.pedometer

import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toLocalDateTime
import hardcoder.dev.logics.features.pedometer.PedometerDailyRateStepsProvider
import hardcoder.dev.logics.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logics.features.pedometer.statistic.PedometerStatisticProvider
import hardcoder.dev.math.safeDiv
import hardcoder.dev.viewmodel.ViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class PedometerViewModel(
    pedometerManager: PedometerManager,
    pedometerTrackProvider: PedometerTrackProvider,
    pedometerStatisticProvider: PedometerStatisticProvider,
    pedometerDailyRateStepsProvider: PedometerDailyRateStepsProvider,
    dateTimeProvider: DateTimeProvider,
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
}