package hardcoder.dev.navigation.splash

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.navigation.dashboard.DashboardScreen
import hardcoder.dev.navigation.user.UserCreationScreen
import hardcoder.dev.presentation.splash.SplashViewModel
import hardcoder.dev.screens.splash.Splash

class SplashScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<SplashViewModel>()

        Splash(
            isFirstLaunchLoadingController = viewModel.isFirstLaunchLoadingController,
            onGoToHeroCreation = {
                navigator replaceAll UserCreationScreen()
            },
            onGoToDashboard = {
                navigator replaceAll DashboardScreen()
            },
        )
    }
}