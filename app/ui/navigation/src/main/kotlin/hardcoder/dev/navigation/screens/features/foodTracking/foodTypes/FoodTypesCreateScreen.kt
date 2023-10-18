package hardcoder.dev.navigation.screens.features.foodTracking.foodTypes

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.presentation.features.foodTracking.foodType.FoodTypeCreationViewModel
import hardcoder.dev.screens.features.foodTracking.foodTypes.FoodTypeCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun FoodTypesCreateScreen(navController: NavController) {
    val viewModel = koinViewModel<FoodTypeCreationViewModel>()

    LaunchedEffectWhenExecuted(
        controller = viewModel.creationController,
        action = navController::popBackStack,
    )

    FoodTypeCreation(
        nameInputController = viewModel.nameInputController,
        iconSelectionController = viewModel.iconSingleSelectionController,
        creationController = viewModel.creationController,
        onGoBack = navController::popBackStack,
    )
}