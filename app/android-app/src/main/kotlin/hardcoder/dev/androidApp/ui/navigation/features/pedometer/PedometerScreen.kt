package hardcoder.dev.androidApp.ui.navigation.features.pedometer

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.features.pedometer.Pedometer
import hardcoder.dev.presentation.features.pedometer.PedometerViewModel
import org.koin.androidx.compose.koinViewModel

class PedometerScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<PedometerViewModel>()

        Pedometer(
            viewModel = viewModel,
            onGoBack = navigator::pop,
            onGoToHistory = {
                navigator += PedometerHistoryScreen()
            },
        )
    }
}