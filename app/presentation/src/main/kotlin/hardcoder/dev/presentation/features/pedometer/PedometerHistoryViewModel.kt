package hardcoder.dev.presentation.features.pedometer

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toLocalDateTime
import hardcoder.dev.logics.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logics.features.pedometer.statistic.PedometerStatisticProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class PedometerHistoryViewModel(
    private val pedometerTrackProvider: PedometerTrackProvider,
    private val pedometerStatisticProvider: PedometerStatisticProvider,
    dateTimeProvider: DateTimeProvider,
) : ScreenModel {

    val dateRangeInputController = InputController(
        coroutineScope = coroutineScope,
        initialInput = dateTimeProvider.currentDateRange(),
    )

    val statisticLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = dateRangeInputController.state.flatMapLatest { range ->
            pedometerStatisticProvider.providePedometerStatistic(range.input)
        },
    )

    val chartEntriesLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = dateRangeInputController.state.flatMapLatest { range ->
            pedometerTrackProvider.providePedometerTracksByRange(range.input)
        }.map { pedometerTracks ->
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
}