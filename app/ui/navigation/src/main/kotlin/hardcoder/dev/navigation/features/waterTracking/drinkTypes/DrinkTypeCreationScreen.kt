package hardcoder.dev.navigation.features.waterTracking.drinkTypes

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeCreationViewModel
import hardcoder.dev.screens.features.waterTracking.drinkType.create.DrinkTypeCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted

class DrinkTypeCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<DrinkTypeCreationViewModel>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.creationController,
            action = navigator::pop,
        )

        DrinkTypeCreation(
            nameInputController = viewModel.nameInputController,
            iconSelectionController = viewModel.iconSelectionController,
            waterPercentageInputController = viewModel.waterPercentageInputController,
            creationController = viewModel.creationController,
            onGoBack = navigator::pop,
        )
    }
}