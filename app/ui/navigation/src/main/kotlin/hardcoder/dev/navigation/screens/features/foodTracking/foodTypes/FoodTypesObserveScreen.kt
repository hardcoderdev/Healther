package hardcoder.dev.navigation.screens.features.foodTracking.foodTypes

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.foodTracking.foodType.FoodTypesViewModel
import hardcoder.dev.screens.features.foodTracking.foodTypes.FoodTypes
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun FoodTypesObserveScreen(navController: NavController) {
    val viewModel = koinViewModel<FoodTypesViewModel>()

    FoodTypes(
        foodTypesLoadingController = viewModel.foodTypesLoadingController,
        onGoBack = navController::popBackStack,
        onCreateFoodType = {
            navController.navigate(Screen.FoodTypesCreate.route)
        },
        onUpdateFoodType = { foodTypeId ->
            navController.navigate(Screen.FoodTypesUpdate.buildRoute(foodTypeId))
        },
    )
}