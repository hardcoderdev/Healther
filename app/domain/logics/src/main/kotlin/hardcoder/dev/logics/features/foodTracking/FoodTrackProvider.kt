package hardcoder.dev.logics.features.foodTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.foodTracking.FoodTrackDao
import hardcoder.dev.database.entities.features.foodTracking.FoodTrack
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
    private val foodTrackDao: FoodTrackDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val foodTypeProvider: FoodTypeProvider,
) {

    fun provideFoodTracksByDayRange(dayRange: ClosedRange<Instant>) =
        foodTrackDao.provideFoodTracksByDayRange(
            startTime = dayRange.start,
            endTime = dayRange.endInclusive,
        ).flatMapLatest { foodTracksList ->
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

    fun provideFoodTrackById(id: Int) = foodTrackDao
        .provideFoodTrackById(id)
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

    fun provideLastFoodTrack() = foodTrackDao
        .provideLastFoodTrack()
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