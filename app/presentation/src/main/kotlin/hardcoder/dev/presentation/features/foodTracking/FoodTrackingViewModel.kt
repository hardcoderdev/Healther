package hardcoder.dev.presentation.features.foodTracking

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logics.features.foodTracking.FoodTrackProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@OptIn(ExperimentalCoroutinesApi::class)
class FoodTrackingViewModel(
    foodTrackProvider: FoodTrackProvider,
    dateTimeProvider: DateTimeProvider,
) : ScreenModel {

    val foodTracksLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = foodTrackProvider.provideFoodTracksByDayRange(
            dayRange = dateTimeProvider.currentDateRange(),
        ),
    )

    val timeSinceLastMealLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = combine(
            foodTrackProvider.provideLastFoodTrack(),
            dateTimeProvider.currentTimeFlow(),
        ) { lastFoodTrack, time ->
            val timeDifference = lastFoodTrack?.creationInstant?.let {
                time.toInstant(TimeZone.currentSystemDefault()) - lastFoodTrack.creationInstant
            } ?: run {
                null
            }

            timeDifference?.inWholeMilliseconds
        },
    )
}