package hardcoder.dev.logics.features.foodTracking

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.FoodTrack
import hardcoder.dev.entities.features.foodTracking.FoodType
import hardcoder.dev.logics.features.foodTracking.foodType.FoodTypeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import hardcoder.dev.entities.features.foodTracking.FoodTrack as FoodTrackEntity

@OptIn(ExperimentalCoroutinesApi::class)
class FoodTrackProvider(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val foodTypeProvider: FoodTypeProvider,
) {

    fun provideFoodTracksByDayRange(dayRange: ClosedRange<Instant>) = appDatabase.foodTrackQueries
        .provideFoodTracksByDayRange(dayRange.start, dayRange.endInclusive)
        .asFlow()
        .map {
            it.executeAsList()
        }.flatMapLatest { foodTracksList ->
            if (foodTracksList.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    foodTracksList.map { foodTrack ->
                        foodTypeProvider.provideFoodTypeById(foodTrack.foodTypeId).map { foodType ->
                            foodTrack.toEntity(foodType = foodType!!)
                        }
                    },
                ) {
                    it.toList()
                }
            }
        }.flowOn(dispatchers.io)

    fun provideFoodTrackById(id: Int) = appDatabase.foodTrackQueries
        .provideFoodTrackById(id)
        .asFlow()
        .map { it.executeAsOneOrNull() }
        .flatMapLatest { foodTrack ->
            if (foodTrack == null) {
                flowOf(null)
            } else {
                combine(
                    foodTypeProvider.provideFoodTypeById(foodTrack.foodTypeId).map { foodType ->
                        foodTrack.toEntity(foodType = foodType!!)
                    },
                ) {
                    it[0]
                }
            }
        }.flowOn(dispatchers.io)

    fun provideLastFoodTrack() = appDatabase.foodTrackQueries
        .provideLastFoodTrack()
        .asFlow()
        .map { it.executeAsOneOrNull() }
        .flatMapLatest { foodTrack ->
            if (foodTrack == null) {
                flowOf(null)
            } else {
                combine(
                    foodTypeProvider.provideFoodTypeById(foodTrack.foodTypeId).map { foodType ->
                        foodTrack.toEntity(foodType = foodType!!)
                    },
                ) {
                    it[0]
                }
            }
        }.flowOn(dispatchers.io)

    private fun FoodTrack.toEntity(
        foodType: FoodType,
    ) = FoodTrackEntity(
        id = id,
        foodType = foodType,
        creationInstant = creationInstant,
        calories = calories,
    )
}