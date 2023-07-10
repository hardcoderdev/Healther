package hardcoder.dev.logic.features.pedometer

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class PedometerStepProvider(
    private val pedometerTrackProvider: PedometerTrackProvider,
    private val dateTimeProvider: DateTimeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideLastSteps(): Flow<Int> = pedometerTrackProvider.providePedometerTracksByRange(
        dateTimeProvider.currentDateRange(),
    ).map { pedometerTracks ->
        pedometerTracks.sumOf { it.stepsCount }
    }.flowOn(dispatchers.io)
}