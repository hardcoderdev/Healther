package hardcoder.dev.androidApp.ui.navigation.features.waterTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack.WaterTracking

class WaterTrackingScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        WaterTracking(
            onGoBack = navigator::pop,
            onHistoryDetails = {
                navigator += WaterTrackingHistoryScreen()
            },
            onSaveWaterTrack = {
                navigator += WaterTrackingCreationScreen()
            },
            onUpdateWaterTrack = { waterTrackId ->
                navigator += WaterTrackingUpdateScreen(waterTrackId)
            }
        )
    }
}