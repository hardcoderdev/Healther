package hardcoder.dev.navigation.screens.features.waterTracking.drinkTypes

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypesViewModel
import hardcoder.dev.screens.features.waterTracking.drinkType.DrinkTypes
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun DrinkTypesObserveScreen(navController: NavController) {
    val viewModel = koinViewModel<DrinkTypesViewModel>()

    DrinkTypes(
        drinkTypesLoadingController = viewModel.drinkTypesLoadingController,
        onGoBack = navController::popBackStack,
        onCreateDrinkType = {
            navController.navigate(Screen.DrinkTypesCreate.route)
        },
        onUpdateDrinkType = { drinkTypeId ->
            navController.navigate(Screen.DrinkTypesUpdate.buildRoute(drinkTypeId))
        },
    )
}