package hardcoder.dev.logic.features.pedometer

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.datetime.createRangeForCurrentDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class PedometerStepProvider(
    private val pedometerTrackProvider: PedometerTrackProvider,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    fun provideLastSteps(): Flow<Int> = pedometerTrackProvider.providePedometerTracksByRange(
        LocalDate.createRangeForCurrentDay()
    ).map { pedometerTracks ->
        pedometerTracks.sumOf { it.stepsCount }
    }.flowOn(dispatchers.io)
}