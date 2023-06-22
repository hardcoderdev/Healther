package hardcoder.dev.androidApp.ui.navigation.features.pedometer

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.pedometer.Pedometer

class PedometerScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Pedometer(
            onGoBack = navigator::pop,
            onGoToPedometerHistory = {
                navigator += PedometerHistoryScreen()
            }
        )
    }
}