package hardcoder.dev.androidApp.ui.navigation.features.waterTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.navigation.features.waterTracking.drinkTypes.DrinkTypesScreen
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingCreationViewModel
import hardcoder.dev.screens.features.waterTracking.waterTrack.create.WaterTrackingCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class WaterTrackingCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<WaterTrackingCreationViewModel>()
        val dateTimeFormatter = koinInject<DateTimeFormatter>()
        val dateTimeProvider = koinInject<DateTimeProvider>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.creationController,
            action = navigator::pop,
        )

        WaterTrackingCreation(
            dateTimeProvider = dateTimeProvider,
            dateTimeFormatter = dateTimeFormatter,
            millilitersDrunkInputController = viewModel.millilitersDrunkInputController,
            drinkSelectionController = viewModel.drinkSelectionController,
            dateInputController = viewModel.dateInputController,
            timeInputController = viewModel.timeInputController,
            creationController = viewModel.creationController,
            onGoBack = navigator::pop,
            onManageDrinkTypes = {
                navigator += DrinkTypesScreen()
            },
        )
    }
}