package hardcoder.dev.navigation.screens.features.foodTracking.foodTracks

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.foodTracking.FoodTrackingCreationViewModel
import hardcoder.dev.screens.features.foodTracking.foodTracks.FoodTrackCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun FoodTracksCreateScreen(navController: NavController) {
    val dateTimeFormatter = koinInject<DateTimeFormatter>()
    val dateTimeProvider = koinInject<DateTimeProvider>()
    val viewModel = koinViewModel<FoodTrackingCreationViewModel>()

    LaunchedEffectWhenExecuted(
        controller = viewModel.creationController,
        action = navController::popBackStack,
    )

    FoodTrackCreation(
        dateTimeProvider = dateTimeProvider,
        dateTimeFormatter = dateTimeFormatter,
        foodSelectionController = viewModel.foodSelectionController,
        caloriesInputController = viewModel.caloriesInputController,
        dateInputController = viewModel.dateInputController,
        timeInputController = viewModel.timeInputController,
        creationController = viewModel.creationController,
        onGoBack = navController::popBackStack,
        onManageFoodTypes = {
            navController.navigate(Screen.FoodTypesObserve.route)
        },
    )
}