package hardcoder.dev.androidApp.ui.navigation.features.pedometer

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.features.pedometer.history.PedometerHistory
import hardcoder.dev.presentation.features.pedometer.PedometerHistoryViewModel
import org.koin.androidx.compose.koinViewModel

class PedometerHistoryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<PedometerHistoryViewModel>()

        PedometerHistory(
            viewModel = viewModel,
            onGoBack = navigator::pop,
        )
    }
}