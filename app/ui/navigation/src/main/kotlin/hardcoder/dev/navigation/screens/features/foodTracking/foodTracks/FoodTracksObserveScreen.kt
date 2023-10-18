package hardcoder.dev.navigation.screens.features.foodTracking.foodTracks

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.formatters.MillisDistanceFormatter
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.foodTracking.FoodTrackingViewModel
import hardcoder.dev.screens.features.foodTracking.foodTracks.FoodTracking
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun FoodTracksObserveScreen(navController: NavController) {
    val millisDistanceFormatter = koinInject<MillisDistanceFormatter>()
    val viewModel = koinViewModel<FoodTrackingViewModel>()

    FoodTracking(
        millisDistanceFormatter = millisDistanceFormatter,
        timeSinceLastMealLoadingController = viewModel.timeSinceLastMealLoadingController,
        foodTracksLoadingController = viewModel.foodTracksLoadingController,
        onGoBack = navController::popBackStack,
        onCreateFoodTrack = {
            navController.navigate(Screen.FoodTracksCreate.route)
        },
        onUpdateFoodTrack = { foodTrackId ->
            navController.navigate(Screen.FoodTracksUpdate.buildRoute(foodTrackId))
        },
    )
}