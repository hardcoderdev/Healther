package hardcoder.dev.logic.features.waterTracking

import hardcoder.dev.datetime.createRangeForCurrentDay
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class WaterTrackingMillilitersDrunkProvider(
    private val waterTrackProvider: WaterTrackProvider,
    private val waterPercentageResolver: WaterPercentageResolver,
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
}