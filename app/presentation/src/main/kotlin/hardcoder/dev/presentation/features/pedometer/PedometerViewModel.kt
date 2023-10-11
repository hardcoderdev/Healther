package hardcoder.dev.presentation.features.pedometer

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toLocalDateTime
import hardcoder.dev.logics.features.pedometer.PedometerDailyRateStepsProvider
import hardcoder.dev.logics.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logics.features.pedometer.statistic.PedometerStatisticProvider
import hardcoder.dev.math.safeDiv
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class PedometerViewModel(
    pedometerManager: PedometerManager,
    pedometerTrackProvider: PedometerTrackProvider,
    pedometerStatisticProvider: PedometerStatisticProvider,
    pedometerDailyRateStepsProvider: PedometerDailyRateStepsProvider,
    dateTimeProvider: DateTimeProvider,
) : ScreenModel {

    val dailyRateProgressController = LoadingController<Float>(
        coroutineScope = coroutineScope,
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
        coroutineScope = coroutineScope,
        flow = pedometerDailyRateStepsProvider.resolve(),
    )

    val statisticLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = pedometerStatisticProvider.providePedometerStatistic(),
    )

    val todayStatisticLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = pedometerStatisticProvider.providePedometerStatistic(
            range = dateTimeProvider.currentDateRange(),
        ),
    )

    val chartEntriesLoadingController = LoadingController(
        coroutineScope = coroutineScope,
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
        coroutineScope = coroutineScope,
        flow = pedometerManager.availability,
    )

    val pedometerToggleController = ToggleController(
        coroutineScope = coroutineScope,
        isActiveFlow = pedometerManager.isTracking,
        toggle = {
            pedometerManager.requestBattery()
            pedometerManager.requestPermissions()
            pedometerManager.toggleTracking()
        },
    )
}