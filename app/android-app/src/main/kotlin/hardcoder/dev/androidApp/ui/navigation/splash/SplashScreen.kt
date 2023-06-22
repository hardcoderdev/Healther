package hardcoder.dev.androidApp.ui.navigation.splash

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.navigation.dashboard.DashboardScreen
import hardcoder.dev.androidApp.ui.splash.Splash

class SplashScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Splash(
            onStartSetUp = {
                navigator += WelcomeScreen()
            },
            onNavigateToDashboard = {
                navigator += DashboardScreen()
            }
        )
    }
}