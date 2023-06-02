package hardcoder.dev.presentation.features.pedometer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.createRangeForThisDay
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatisticProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class PedometerHistoryViewModel(
    private val pedometerTrackProvider: PedometerTrackProvider,
    private val pedometerStatisticProvider: PedometerStatisticProvider
) : ViewModel() {

    val dateRangeInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = LocalDate.now().createRangeForThisDay()
    )

    val statisticLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = dateRangeInputController.state.flatMapLatest { range ->
            pedometerStatisticProvider.providePedometerStatistic(range.input)
        }
    )

    val chartEntriesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = dateRangeInputController.state.flatMapLatest { range ->
            pedometerTrackProvider.providePedometerTracksByRange(range.input)
        }.map { pedometerTracks ->
            pedometerTracks.groupBy {
                it.range.start.toLocalDateTime(TimeZone.currentSystemDefault()).hour
            }.map { entry ->
                entry.key to entry.value.sumOf { it.stepsCount }
            }
        }
    )
}