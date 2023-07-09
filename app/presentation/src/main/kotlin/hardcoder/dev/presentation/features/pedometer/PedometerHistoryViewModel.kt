package hardcoder.dev.presentation.features.pedometer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toLocalDateTime
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatisticProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class PedometerHistoryViewModel(
    private val pedometerTrackProvider: PedometerTrackProvider,
    private val pedometerStatisticProvider: PedometerStatisticProvider,
    dateTimeProvider: DateTimeProvider,
) : ViewModel() {

    val dateRangeInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = dateTimeProvider.currentDateRange(),
    )

    val statisticLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = dateRangeInputController.state.flatMapLatest { range ->
            pedometerStatisticProvider.providePedometerStatistic(range.input)
        },
    )

    val chartEntriesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = dateRangeInputController.state.flatMapLatest { range ->
            pedometerTrackProvider.providePedometerTracksByRange(range.input)
        }.map { pedometerTracks ->
            pedometerTracks.groupBy {
                it.range.start.toLocalDateTime().hour
            }.map { entry ->
                entry.key to entry.value.sumOf { it.stepsCount }
            }
        },
    )
}