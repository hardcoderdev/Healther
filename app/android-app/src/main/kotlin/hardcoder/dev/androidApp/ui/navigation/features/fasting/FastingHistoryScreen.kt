package hardcoder.dev.androidApp.ui.navigation.features.fasting

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.features.fasting.history.FastingHistory
import hardcoder.dev.presentation.features.fasting.FastingHistoryViewModel
import org.koin.androidx.compose.koinViewModel

class FastingHistoryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<FastingHistoryViewModel>()

        FastingHistory(
            viewModel = viewModel,
            onGoBack = navigator::pop,
        )
    }
}