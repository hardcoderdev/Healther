package hardcoder.dev.androidApp.ui.navigation.features.fasting

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.fasting.Fasting

class FastingScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Fasting(
            onGoBack = navigator::pop,
            onHistoryDetails = {
                navigator += FastingHistoryScreen()
            },
            onCreateTrack = {
                navigator += FastingCreationScreen()
            }
        )
    }
}