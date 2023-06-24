package hardcoder.dev.logic.features.waterTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.datetime.createRangeForCurrentDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class WaterTrackingMillilitersDrunkProvider(
    private val waterTrackProvider: WaterTrackProvider,
    private val waterPercentageResolver: WaterPercentageResolver,
    private val waterTrackingDailyRateProvider: WaterTrackingDailyRateProvider,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    private fun provideMillilitersDrunkToday(): Flow<Int> {
        return waterTrackProvider.provideWaterTracksByDayRange(
            LocalDate.createRangeForCurrentDay()
        ).map { waterTrackList ->
            waterTrackList.sumOf { waterTrack ->
                waterPercentageResolver.resolve(
                    drinkType = waterTrack.drinkType,
                    millilitersDrunk = waterTrack.millilitersCount
                )
            }
        }.flowOn(dispatchers.io)
    }

    fun provideMillilitersDrunkToDailyRateToday() = combine(
        provideMillilitersDrunkToday(),
        waterTrackingDailyRateProvider.provideDailyRateInMilliliters(),
        ::MillilitersDrunkToDailyRate
    ).flowOn(dispatchers.default)
}