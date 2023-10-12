package hardcoder.dev.navigation.features.foodTracking.foodTracks

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.formatters.MillisDistanceFormatter
import hardcoder.dev.presentation.features.foodTracking.FoodTrackingViewModel
import hardcoder.dev.screens.features.foodTracking.foodTracks.FoodTracking
import org.koin.compose.koinInject

class FoodTrackingScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<FoodTrackingViewModel>()
        val millisDistanceFormatter = koinInject<MillisDistanceFormatter>()

        FoodTracking(
            millisDistanceFormatter = millisDistanceFormatter,
            timeSinceLastMealLoadingController = viewModel.timeSinceLastMealLoadingController,
            foodTracksLoadingController = viewModel.foodTracksLoadingController,
            onGoBack = navigator::pop,
            onCreateFoodTrack = {
                navigator += FoodTrackingCreationScreen()
            },
            onUpdateFoodTrack = { foodTrackId ->
                navigator += FoodTrackingUpdateScreen(foodTrackId)
            },
        )
    }
}