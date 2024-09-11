package hardcoder.dev.navigation.features.foodTracking.foodTypes

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.foodTracking.foodType.FoodTypeCreationViewModel
import hardcoder.dev.screens.features.foodTracking.foodTypes.create.FoodTypeCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted

class FoodTypeCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<FoodTypeCreationViewModel>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.creationController,
            action = navigator::pop,
        )

        FoodTypeCreation(
            nameInputController = viewModel.nameInputController,
            iconSelectionController = viewModel.iconSingleSelectionController,
            creationController = viewModel.creationController,
            onGoBack = navigator::pop,
        )
    }
}