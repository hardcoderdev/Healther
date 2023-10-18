package hardcoder.dev.navigation.screens.features.waterTracking.drinkTypes

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeCreationViewModel
import hardcoder.dev.screens.features.waterTracking.drinkType.create.DrinkTypeCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun DrinkTypesCreateScreen(navController: NavController) {
    val viewModel = koinViewModel<DrinkTypeCreationViewModel>()

    LaunchedEffectWhenExecuted(
        controller = viewModel.creationController,
        action = navController::popBackStack,
    )

    DrinkTypeCreation(
        nameInputController = viewModel.nameInputController,
        iconSelectionController = viewModel.iconSelectionController,
        waterPercentageInputController = viewModel.waterPercentageInputController,
        creationController = viewModel.creationController,
        onGoBack = navController::popBackStack,
    )
}