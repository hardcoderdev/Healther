package hardcoder.dev.presentation.features.waterTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.coroutines.mapItems
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.millisToLocalDateTime
import hardcoder.dev.logic.features.waterTracking.resolvers.WaterPercentageResolver
import hardcoder.dev.logic.features.waterTracking.WaterTrackProvider
import hardcoder.dev.logic.features.waterTracking.statistic.WaterTrackingStatisticProvider
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class WaterTrackingAnalyticsViewModel(
    waterTrackingStatisticProvider: WaterTrackingStatisticProvider,
    waterTrackProvider: WaterTrackProvider,
    dateTimeProvider: DateTimeProvider,
    waterPercentageResolver: WaterPercentageResolver,
) : ViewModel() {

    private val waterTracksLoadingController = LoadingController(
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

    val statisticLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = waterTrackingStatisticProvider.provideWaterTrackingStatistic(),
    )

    val chartEntriesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = waterTracksLoadingController.state.map {
            (it as? LoadingController.State.Loaded)?.data
        }.filterNotNull().map { waterTrackList ->
            waterTrackList.groupBy {
                it.timeInMillis.millisToLocalDateTime().hour
            }.map { entry ->
                entry.key to entry.value.sumOf { it.millilitersCount }
            }
        },
    )
}