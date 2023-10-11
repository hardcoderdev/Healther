package hardcoder.dev.presentation.features.waterTracking

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.coroutines.mapItems
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.millisToLocalDateTime
import hardcoder.dev.logics.features.waterTracking.WaterTrackProvider
import hardcoder.dev.logics.features.waterTracking.WaterTrackingStatisticProvider
import hardcoder.dev.resolvers.features.waterTracking.WaterPercentageResolver
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class WaterTrackingAnalyticsViewModel(
    waterTrackingStatisticProvider: WaterTrackingStatisticProvider,
    waterTrackProvider: WaterTrackProvider,
    dateTimeProvider: DateTimeProvider,
    waterPercentageResolver: WaterPercentageResolver,
) : ScreenModel {

    private val waterTracksLoadingController = LoadingController(
        coroutineScope = coroutineScope,
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
        coroutineScope = coroutineScope,
        flow = waterTrackingStatisticProvider.provideWaterTrackingStatistic(),
    )

    val chartEntriesLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = waterTracksLoadingController.state.map {
            (it as? LoadingController.State.Loaded)?.data
        }.filterNotNull().map { waterTrackList ->
            WaterTrackingChartData(
                entriesList = waterTrackList.groupBy {
                    it.timeInMillis.millisToLocalDateTime().hour
                }.map { entry ->
                    WaterTrackingChartEntry(
                        from = entry.key,
                        to = entry.value.sumOf { it.millilitersCount },
                    )
                },
            )
        },
    )
}