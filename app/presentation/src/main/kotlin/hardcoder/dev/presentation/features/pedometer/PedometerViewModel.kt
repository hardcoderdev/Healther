package hardcoder.dev.presentation.features.pedometer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toLocalDateTime
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatisticProvider
import hardcoder.dev.math.safeDiv
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class PedometerViewModel(
    pedometerManager: PedometerManager,
    pedometerTrackProvider: PedometerTrackProvider,
    pedometerStatisticProvider: PedometerStatisticProvider,
    dateTimeProvider: DateTimeProvider,
) : ViewModel() {

    val dailyRateProgressController = LoadingController<Float>(
        coroutineScope = viewModelScope,
        flow = pedometerStatisticProvider.providePedometerStatistic(
            range = dateTimeProvider.currentDateRange(),
        ).map {
            it.totalSteps safeDiv DAILY_RATE_PEDOMETER
        },
    )

    val dailyRateStepsLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = flowOf(DAILY_RATE_PEDOMETER), // TODO nice ok da
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
            pedometerTracks.groupBy {
                it.range.start.toLocalDateTime().hour
            }.map { entry ->
                entry.key to entry.value.sumOf { it.stepsCount }
            }
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

    internal companion object {
        private const val DAILY_RATE_PEDOMETER = 6000
    }
}