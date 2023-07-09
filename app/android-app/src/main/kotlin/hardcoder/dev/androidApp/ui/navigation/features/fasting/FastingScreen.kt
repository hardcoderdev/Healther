package hardcoder.dev.androidApp.ui.navigation.features.fasting

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.features.fasting.FastingInitial
import hardcoder.dev.presentation.features.fasting.FastingViewModel
import org.koin.androidx.compose.koinViewModel

class FastingScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<FastingViewModel>()

        FastingInitial(
            viewModel = viewModel,
            onGoBack = navigator::pop,
            onCreateFastingTrack = {
                navigator += FastingCreationScreen()
            },
            onGoToFastingHistory = {
                navigator += FastingHistoryScreen()
            },
        )
    }
}