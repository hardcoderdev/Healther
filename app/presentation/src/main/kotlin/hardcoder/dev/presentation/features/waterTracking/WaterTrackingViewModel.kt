package hardcoder.dev.presentation.features.waterTracking

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.coroutines.mapItems
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logics.features.waterTracking.WaterTrackProvider
import hardcoder.dev.logics.features.waterTracking.WaterTrackingMillilitersDrunkProvider
import hardcoder.dev.math.safeDiv
import hardcoder.dev.resolvers.features.waterTracking.WaterPercentageResolver
import kotlinx.coroutines.flow.map

class WaterTrackingViewModel(
    waterTrackProvider: WaterTrackProvider,
    waterPercentageResolver: WaterPercentageResolver,
    millilitersDrunkProvider: WaterTrackingMillilitersDrunkProvider,
    dateTimeProvider: DateTimeProvider,
) : ScreenModel {

    val dailyRateProgressController = LoadingController<Float>(
        coroutineScope = coroutineScope,
        flow = millilitersDrunkProvider.provideMillilitersDrunkToDailyRateToday(
            dateRange = dateTimeProvider.currentDateRange(),
        ).map {
            it.millilitersDrunkCount safeDiv it.dailyWaterIntake
        },
    )

    val waterTracksLoadingController = LoadingController(
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

    val millilitersDrunkLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = millilitersDrunkProvider.provideMillilitersDrunkToDailyRateToday(
            dateRange = dateTimeProvider.currentDateRange(),
        ),
    )
}