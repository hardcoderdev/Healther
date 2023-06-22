package hardcoder.dev.androidApp.ui.navigation.features.waterTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack.update.WaterTrackingUpdate
import hardcoder.dev.androidApp.ui.navigation.features.waterTracking.drinkTypes.ManageDrinkTypesScreen

data class WaterTrackingUpdateScreen(val waterTrackId: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        WaterTrackingUpdate(
            waterTrackId = waterTrackId,
            onGoBack = navigator::pop,
            onManageDrinkType = {
                navigator += ManageDrinkTypesScreen()
            }
        )
    }
}
