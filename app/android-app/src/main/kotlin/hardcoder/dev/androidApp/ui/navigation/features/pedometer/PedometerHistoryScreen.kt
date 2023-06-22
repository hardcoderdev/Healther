package hardcoder.dev.androidApp.ui.navigation.features.pedometer

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.pedometer.history.PedometerHistory

class PedometerHistoryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        PedometerHistory(
            onGoBack = navigator::pop
        )
    }
}