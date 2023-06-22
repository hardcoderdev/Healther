package hardcoder.dev.logic.features.pedometer.statistic

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.logic.features.pedometer.CaloriesResolver
import hardcoder.dev.logic.features.pedometer.KilometersResolver
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.math.safeAverage
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds

class PedometerStatisticProvider(
    private val kilometersResolver: KilometersResolver,
    private val caloriesResolver: CaloriesResolver,
    private val pedometerTrackProvider: PedometerTrackProvider,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    fun providePedometerStatistic(
        range: ClosedRange<Instant> = Instant.DISTANT_PAST..Instant.DISTANT_FUTURE
    ) = pedometerTrackProvider.providePedometerTracksByRange(range).map { pedometerTracks ->
        val totalSteps = pedometerTracks.sumOf { it.stepsCount }

        val totalKilometers = kilometersResolver.resolve(totalSteps)

        val totalDuration = pedometerTracks.sumOf {
            it.range.endInclusive.epochSeconds - it.range.start.epochSeconds
        }.seconds

        val totalCalories = caloriesResolver.resolve(totalSteps)

        val averageSteps = pedometerTracks.map { it.stepsCount }.safeAverage().roundToInt()

        val averageKilometers = pedometerTracks.map {
            kilometersResolver.resolve(it.stepsCount)
        }.safeAverage().toFloat()

        val averageDuration = pedometerTracks.map {
            it.range.endInclusive.epochSeconds - it.range.start.epochSeconds
        }.safeAverage().seconds

        val averageCalories = pedometerTracks.map {
            caloriesResolver.resolve(it.stepsCount)
        }.safeAverage().toFloat()

        PedometerStatistic(
            totalSteps = totalSteps,
            totalKilometers = totalKilometers,
            totalDuration = totalDuration,
            totalCalories = totalCalories,
            averageSteps = averageSteps,
            averageKilometers = averageKilometers,
            averageDuration = averageDuration,
            averageCalories = averageCalories
        )
    }.flowOn(dispatchers.io)
}