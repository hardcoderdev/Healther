package hardcoder.dev.logic.features.pedometer

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.PedometerTrack
import hardcoder.dev.logic.reward.currency.CurrencyProvider
import hardcoder.dev.logic.features.FeatureType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import hardcoder.dev.logic.features.pedometer.PedometerTrack as PedometerTrackEntity

class PedometerTrackProvider(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val currencyProvider: CurrencyProvider,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun providePedometerTracksByRange(range: ClosedRange<Instant>) = appDatabase.pedometerTrackQueries
        .providePedometerTracksByRange(
            range.start,
            range.endInclusive,
            range.start,
            range.endInclusive,
        )
        .asFlow()
        .map { it.executeAsList() }
        .flatMapLatest { pedometerTrackList ->
            if (pedometerTrackList.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    pedometerTrackList.map { pedometerTrack ->
                        combine(
                            currencyProvider.isTrackCollected(
                                featureType = FeatureType.PEDOMETER,
                                linkedTrackId = pedometerTrack.id,
                            ),
                        ) {
                            pedometerTrack.toEntity(isRewardCollected = it[0])
                        }
                    },
                ) {
                    it.toList()
                }
            }
        }.flowOn(dispatchers.io)

    private fun PedometerTrack.toEntity(isRewardCollected: Boolean) = PedometerTrackEntity(
        id = id,
        stepsCount = stepsCount,
        range = startTime..endTime,
        isRewardCollected = isRewardCollected,
    )
}