package hardcoder.dev.androidApp.ui.navigation.features.waterTracking.drinkTypes

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.waterTracking.drinkType.ManageDrinkTypes

class ManageDrinkTypesScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        ManageDrinkTypes(
            onGoBack = navigator::pop,
            onCreateDrinkType = {
                navigator += DrinkTypeCreationScreen()
            },
            onUpdateDrinkType = { drinkTypeId ->
                navigator += DrinkTypeUpdateScreen(drinkTypeId)
            }
        )
    }
}