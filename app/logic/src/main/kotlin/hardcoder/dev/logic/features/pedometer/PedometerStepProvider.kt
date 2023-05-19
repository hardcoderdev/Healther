package hardcoder.dev.logic.features.pedometer

import hardcoder.dev.datetime.createRangeForCurrentDay
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class PedometerStepProvider(private val pedometerTrackProvider: PedometerTrackProvider) {

    fun provideLastSteps(): Flow<Int> = pedometerTrackProvider.providePedometerTracksByRange(
        LocalDate.now().createRangeForCurrentDay()
    ).map { pedometerTracks ->
        pedometerTracks.sumOf { it.stepsCount }
    }
}