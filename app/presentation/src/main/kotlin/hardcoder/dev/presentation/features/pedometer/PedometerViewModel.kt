package hardcoder.dev.presentation.features.pedometer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.datetime.createRangeForCurrentDay
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatisticProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class PedometerViewModel(
    pedometerManager: PedometerManager,
    pedometerTrackProvider: PedometerTrackProvider,
    pedometerStatisticProvider: PedometerStatisticProvider,
) : ViewModel() {

    val dailyRateStepsLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = flowOf(DAILY_RATE_PEDOMETER) // TODO nice ok da
    )

    val statisticLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = pedometerStatisticProvider.providePedometerStatistic()
    )

    val todayStatisticLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = pedometerStatisticProvider.providePedometerStatistic(
            range = LocalDate.now().createRangeForCurrentDay(TimeZone.currentSystemDefault())
        )
    )

    val chartEntriesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = pedometerTrackProvider.providePedometerTracksByRange(
            LocalDate.now().createRangeForCurrentDay(timeZone = TimeZone.currentSystemDefault())
        ).map { pedometerTracks ->
            pedometerTracks.groupBy {
                it.range.start.toLocalDateTime(TimeZone.currentSystemDefault()).hour
            }.map { entry ->
                entry.key to entry.value.sumOf { it.stepsCount }
            }
        }
    )

    val pedometerAvailabilityLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = pedometerManager.availability
    )

    val pedometerToggleController = ToggleController(
        coroutineScope = viewModelScope,
        isActiveFlow = pedometerManager.isTracking,
        toggle = {
            pedometerManager.requestBattery()
            pedometerManager.requestPermissions()
            pedometerManager.toggleTracking()
        }
    )

    internal companion object {
        private const val DAILY_RATE_PEDOMETER = 6000
    }
}