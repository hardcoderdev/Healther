package hardcoder.dev.navigation.screens.features.waterTracking.waterTracks

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingCreationViewModel
import hardcoder.dev.screens.features.waterTracking.waterTrack.create.WaterTrackingCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun WaterTracksCreateScreen(navController: NavController) {
    val viewModel = koinViewModel<WaterTrackingCreationViewModel>()
    val dateTimeFormatter = koinInject<DateTimeFormatter>()
    val dateTimeProvider = koinInject<DateTimeProvider>()

    LaunchedEffectWhenExecuted(
        controller = viewModel.creationController,
        action = navController::popBackStack,
    )

    WaterTrackingCreation(
        dateTimeProvider = dateTimeProvider,
        dateTimeFormatter = dateTimeFormatter,
        millilitersDrunkInputController = viewModel.millilitersDrunkInputController,
        drinkSelectionController = viewModel.drinkSelectionController,
        dateInputController = viewModel.dateInputController,
        timeInputController = viewModel.timeInputController,
        creationController = viewModel.creationController,
        onGoBack = navController::popBackStack,
        onManageDrinkTypes = {
            navController.navigate(Screen.DrinkTypesObserve.route)
        },
    )
}