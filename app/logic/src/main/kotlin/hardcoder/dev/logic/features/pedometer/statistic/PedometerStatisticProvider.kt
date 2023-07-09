package hardcoder.dev.logic.features.pedometer.statistic

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.logic.features.pedometer.CaloriesResolver
import hardcoder.dev.logic.features.pedometer.KilometersResolver
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

class PedometerStatisticProvider(
    private val kilometersResolver: KilometersResolver,
    private val caloriesResolver: CaloriesResolver,
    private val pedometerTrackProvider: PedometerTrackProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun providePedometerStatistic(
        range: ClosedRange<Instant> = Instant.DISTANT_PAST..Instant.DISTANT_FUTURE,
    ) = pedometerTrackProvider.providePedometerTracksByRange(range).map { pedometerTracks ->
        val totalSteps = pedometerTracks.sumOf { it.stepsCount }

        val totalKilometers = kilometersResolver.resolve(totalSteps)

        val totalDuration = pedometerTracks.sumOf {
            it.range.endInclusive.epochSeconds - it.range.start.epochSeconds
        }.seconds

        val totalCalories = caloriesResolver.resolve(totalSteps)

        PedometerStatistic(
            totalSteps = totalSteps,
            totalKilometers = totalKilometers,
            totalDuration = totalDuration,
            totalCalories = totalCalories,
        )
    }.flowOn(dispatchers.io)
}