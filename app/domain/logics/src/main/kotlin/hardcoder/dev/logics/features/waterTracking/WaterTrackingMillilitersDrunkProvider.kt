package hardcoder.dev.logics.features.waterTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.entities.features.waterTracking.MillilitersDrunkToDailyRate
import hardcoder.dev.resolvers.features.waterTracking.WaterPercentageResolver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

class WaterTrackingMillilitersDrunkProvider(
    private val waterTrackProvider: WaterTrackProvider,
    private val waterPercentageResolver: WaterPercentageResolver,
    private val waterTrackingDailyRateProvider: WaterTrackingDailyRateProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    private fun provideMillilitersDrunk(
        dateRange: ClosedRange<Instant>,
    ): Flow<Int> {
        return waterTrackProvider.provideWaterTracksByDayRange(
            dayRange = dateRange,
        ).map { waterTrackList ->
            waterTrackList.sumOf { waterTrack ->
                waterPercentageResolver.resolve(
                    drinkType = waterTrack.drinkType,
                    millilitersDrunk = waterTrack.millilitersCount,
                )
            }
        }.flowOn(dispatchers.io)
    }

    fun provideMillilitersDrunkToDailyRateToday(
        dateRange: ClosedRange<Instant>,
    ) = combine(
        provideMillilitersDrunk(dateRange),
        waterTrackingDailyRateProvider.provideDailyRateInMilliliters(),
        ::MillilitersDrunkToDailyRate,
    ).flowOn(dispatchers.default)
}