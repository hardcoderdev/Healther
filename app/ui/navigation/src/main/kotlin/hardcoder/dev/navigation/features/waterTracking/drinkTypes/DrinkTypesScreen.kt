package hardcoder.dev.navigation.features.waterTracking.drinkTypes

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypesViewModel
import hardcoder.dev.screens.features.waterTracking.drinkType.observe.DrinkTypes

class DrinkTypesScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<DrinkTypesViewModel>()

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