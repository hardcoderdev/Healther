package hardcoder.dev.logic.features.pedometer.statistic

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.PedometerTrack
import hardcoder.dev.entities.features.pedometer.statistic.PedometerStatistic
import hardcoder.dev.logic.features.pedometer.CaloriesResolver
import hardcoder.dev.logic.features.pedometer.KilometersResolver
import kotlinx.coroutines.flow.map
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds
import hardcoder.dev.entities.features.pedometer.PedometerTrack as PedometerTrackEntity

class PedometerStatisticProvider(
    private val appDatabase: AppDatabase,
    private val kilometersResolver: KilometersResolver,
    private val caloriesResolver: CaloriesResolver
) {

    fun providePedometerStatistic() = appDatabase.pedometerTrackQueries
        .provideAllPedometerTracks()
        .asFlow()
        .map { pedometerTrackQuery ->
            pedometerTrackQuery.executeAsList().map {
                it.toEntity()
            }
        }.map { pedometerTracks ->
            val totalSteps = pedometerTracks.takeIf {
                it.isNotEmpty()
            }?.sumOf { it.stepsCount }

            val totalKilometers = totalSteps?.let {
                kilometersResolver.resolve(it)
            }

            val totalDuration = pedometerTracks.takeIf {
                it.isNotEmpty()
            }?.sumOf {
                it.range.last - it.range.first
            }?.milliseconds

            val totalCalories = totalSteps?.let {
                caloriesResolver.resolve(totalSteps)
            }

            val averageSteps = pedometerTracks.takeIf {
                it.isNotEmpty()
            }?.map {
                it.stepsCount
            }?.average()?.roundToInt()

            val averageKilometers = pedometerTracks.takeIf {
                it.isNotEmpty()
            }?.map {
                kilometersResolver.resolve(it.stepsCount)
            }?.average()?.toFloat()

            val averageDuration = pedometerTracks.takeIf {
                it.isNotEmpty()
            }?.map {
                it.range.last - it.range.first
            }?.average()?.milliseconds

            val averageCalories = pedometerTracks.takeIf {
                it.isNotEmpty()
            }?.map {
                caloriesResolver.resolve(it.stepsCount)
            }?.average()?.toFloat()

            if (pedometerTracks.isNotEmpty()) {
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
            } else {
                null
            }
        }

    private fun PedometerTrack.toEntity() = PedometerTrackEntity(
        id, stepsCount, startTime..endTime
    )
}