package hardcoder.dev.logic.features.waterTracking

import hardcoder.dev.datetime.createRangeForCurrentDay
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class WaterTrackingMillilitersDrunkProvider(
    private val waterTrackProvider: WaterTrackProvider,
    private val waterPercentageResolver: WaterPercentageResolver,
    private val waterTrackingDailyRateProvider: WaterTrackingDailyRateProvider
) {

    fun provideMillilitersDrunkToday(): Flow<Int> {
        return waterTrackProvider.provideWaterTracksByDayRange(
            LocalDate.now().createRangeForCurrentDay()
        ).map { waterTrackList ->
            waterTrackList.sumOf { waterTrack ->
                waterPercentageResolver.resolve(
                    drinkType = waterTrack.drinkType,
                    millilitersDrunk = waterTrack.millilitersCount
                )
            }
        }
    }

    fun provideMillilitersDrunkToDailyRateToday() = combine(
        provideMillilitersDrunkToday(),
        waterTrackingDailyRateProvider.provideDailyRateInMilliliters(),
        ::MillilitersDrunkToDailyRate
    ).flowOn(Dispatchers.Default)
}