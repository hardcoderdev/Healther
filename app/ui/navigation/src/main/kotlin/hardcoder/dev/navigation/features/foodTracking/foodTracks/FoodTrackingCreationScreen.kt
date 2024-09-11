package hardcoder.dev.navigation.features.foodTracking.foodTracks

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.navigation.features.foodTracking.foodTypes.FoodTypesScreen
import hardcoder.dev.presentation.features.foodTracking.FoodTrackingCreationViewModel
import hardcoder.dev.screens.features.foodTracking.foodTracks.create.FoodTrackCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.compose.koinInject

class FoodTrackingCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<FoodTrackingCreationViewModel>()
        val dateTimeFormatter = koinInject<DateTimeFormatter>()
        val dateTimeProvider = koinInject<DateTimeProvider>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.creationController,
            action = navigator::pop,
        )

        FoodTrackCreation(
            dateTimeProvider = dateTimeProvider,
            dateTimeFormatter = dateTimeFormatter,
            foodSelectionController = viewModel.foodSelectionController,
            caloriesInputController = viewModel.caloriesInputController,
            dateInputController = viewModel.dateInputController,
            timeInputController = viewModel.timeInputController,
            creationController = viewModel.creationController,
            onGoBack = navigator::pop,
            onManageFoodTypes = {
                navigator += FoodTypesScreen()
            },
        )
    }
}