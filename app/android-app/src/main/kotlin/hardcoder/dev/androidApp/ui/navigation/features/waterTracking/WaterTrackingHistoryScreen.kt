package hardcoder.dev.androidApp.ui.navigation.features.waterTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack.history.WaterTrackingHistory

class WaterTrackingHistoryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        WaterTrackingHistory(
            onGoBack = navigator::pop,
            onWaterTrackUpdate = { waterTrack ->
                navigator += WaterTrackingUpdateScreen(waterTrack.id)
            }
        )
    }
}