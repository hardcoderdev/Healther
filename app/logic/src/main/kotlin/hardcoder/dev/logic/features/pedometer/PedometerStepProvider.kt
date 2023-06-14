package hardcoder.dev.logic.features.pedometer

import hardcoder.dev.datetime.createRangeForThisDay
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class PedometerStepProvider(
    private val pedometerTrackProvider: PedometerTrackProvider,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun provideLastSteps(): Flow<Int> = pedometerTrackProvider.providePedometerTracksByRange(
        LocalDate.now().createRangeForThisDay()
    ).map { pedometerTracks ->
        pedometerTracks.sumOf { it.stepsCount }
    }.flowOn(ioDispatcher)
}