package hardcoder.dev.androidApp.ui.navigation.features.waterTracking.drinkTypes

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.features.waterTracking.drinkType.DrinkTypes
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypesViewModel
import org.koin.androidx.compose.koinViewModel

class DrinkTypesScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<DrinkTypesViewModel>()

        DrinkTypes(
            drinkTypesLoadingController = viewModel.drinkTypesLoadingController,
            onGoBack = navigator::pop,
            onCreateDrinkType = {
                navigator += DrinkTypeCreationScreen()
            },
            onUpdateDrinkType = { drinkTypeId ->
                navigator += DrinkTypeUpdateScreen(drinkTypeId)
            },
        )
    }
}