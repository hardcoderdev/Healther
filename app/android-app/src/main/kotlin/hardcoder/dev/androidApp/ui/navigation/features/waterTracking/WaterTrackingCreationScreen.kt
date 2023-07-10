package hardcoder.dev.androidApp.ui.navigation.features.waterTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.navigation.features.waterTracking.drinkTypes.DrinkTypesScreen
import hardcoder.dev.androidApp.ui.screens.features.waterTracking.waterTrack.create.WaterTrackingCreation
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingCreationViewModel
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel

class WaterTrackingCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<WaterTrackingCreationViewModel>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.creationController,
            action = navigator::pop,
        )

        WaterTrackingCreation(
            viewModel = viewModel,
            onGoBack = navigator::pop,
            onManageDrinkTypes = {
                navigator += DrinkTypesScreen()
            },
        )
    }
}