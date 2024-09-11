package hardcoder.dev.navigation.features.foodTracking.foodTypes

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.foodTracking.foodType.FoodTypesViewModel
import hardcoder.dev.screens.features.foodTracking.foodTypes.observe.FoodTypes

class FoodTypesScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<FoodTypesViewModel>()

        FoodTypes(
            foodTypesLoadingController = viewModel.foodTypesLoadingController,
            onGoBack = navigator::pop,
            onCreateFoodType = {
                navigator += FoodTypeCreationScreen()
            },
            onUpdateFoodType = { foodTypeId ->
                navigator += FoodTypeUpdateScreen(foodTypeId)
            },
        )
    }
}