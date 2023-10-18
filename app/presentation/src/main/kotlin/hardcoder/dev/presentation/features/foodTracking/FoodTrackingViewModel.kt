package hardcoder.dev.presentation.features.foodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logics.features.foodTracking.FoodTrackProvider
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class FoodTrackingViewModel(
    foodTrackProvider: FoodTrackProvider,
    dateTimeProvider: DateTimeProvider,
) : ViewModel() {

    val foodTracksLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = foodTrackProvider.provideFoodTracksByDayRange(
            dayRange = dateTimeProvider.currentDateRange(),
        ),
    )

    val timeSinceLastMealLoadingController = LoadingController(
        coroutineScope = viewModelScope,
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