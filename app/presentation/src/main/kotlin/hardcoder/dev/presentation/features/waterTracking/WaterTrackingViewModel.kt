package hardcoder.dev.presentation.features.waterTracking

import hardcoder.dev.controller.LoadingController
import hardcoder.dev.coroutines.mapItems
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logics.features.waterTracking.WaterTrackProvider
import hardcoder.dev.logics.features.waterTracking.WaterTrackingMillilitersDrunkProvider
import hardcoder.dev.math.safeDiv
import hardcoder.dev.resolvers.features.waterTracking.WaterPercentageResolver
import hardcoder.dev.viewmodel.ViewModel
import kotlinx.coroutines.flow.map

class WaterTrackingViewModel(
    waterTrackProvider: WaterTrackProvider,
    waterPercentageResolver: WaterPercentageResolver,
    millilitersDrunkProvider: WaterTrackingMillilitersDrunkProvider,
    dateTimeProvider: DateTimeProvider,
) : ViewModel() {

    val dailyRateProgressController = LoadingController<Float>(
        coroutineScope = viewModelScope,
        flow = millilitersDrunkProvider.provideMillilitersDrunkToDailyRateToday(
            dateRange = dateTimeProvider.currentDateRange(),
        ).map {
            it.millilitersDrunkCount safeDiv it.dailyWaterIntake
        },
    )

    val waterTracksLoadingController = LoadingController(
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

    val millilitersDrunkLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = millilitersDrunkProvider.provideMillilitersDrunkToDailyRateToday(
            dateRange = dateTimeProvider.currentDateRange(),
        ),
    )
}