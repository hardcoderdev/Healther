package hardcoder.dev.presentation.features.waterTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.coroutines.mapItems
import hardcoder.dev.datetime.createRangeForCurrentDay
import hardcoder.dev.datetime.millisToLocalDateTime
import hardcoder.dev.logic.features.waterTracking.WaterPercentageResolver
import hardcoder.dev.logic.features.waterTracking.WaterTrackProvider
import hardcoder.dev.logic.features.waterTracking.WaterTrackingMillilitersDrunkProvider
import hardcoder.dev.logic.features.waterTracking.statistic.WaterTrackingStatisticProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class WaterTrackingViewModel(
    waterTrackProvider: WaterTrackProvider,
    waterPercentageResolver: WaterPercentageResolver,
    waterTrackingStatisticProvider: WaterTrackingStatisticProvider,
    millilitersDrunkProvider: WaterTrackingMillilitersDrunkProvider
) : ViewModel() {

    val waterTracksLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = waterTrackProvider.provideWaterTracksByDayRange(
            LocalDate.now().createRangeForCurrentDay()
        ).mapItems { waterTrack ->
            waterTrack.toItem(
                resolvedMillilitersCount = waterPercentageResolver.resolve(
                    drinkType = waterTrack.drinkType,
                    millilitersDrunk = waterTrack.millilitersCount
                )
            )
        }
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
        }
    )

    val millilitersDrunkLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = millilitersDrunkProvider.provideMillilitersDrunkToDailyRateToday()
    )

    val statisticLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = waterTrackingStatisticProvider.provideWaterTrackingStatistic()
    )
}