package hardcoder.dev.androidApp.ui.navigation.features.fasting

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.fasting.history.FastingHistory

class FastingHistoryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        FastingHistory(
            onGoBack = navigator::pop
        )
    }
}