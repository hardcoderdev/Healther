package hardcoder.dev.androidApp.ui.navigation.splash

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.navigation.dashboard.DashboardScreen
import hardcoder.dev.androidApp.ui.navigation.hero.HeroCreationScreen
import hardcoder.dev.androidApp.ui.navigation.hero.HeroDeathScreen
import hardcoder.dev.androidApp.ui.screens.splash.Splash
import hardcoder.dev.presentation.splash.SplashViewModel
import org.koin.androidx.compose.koinViewModel

class SplashScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<SplashViewModel>()

        Splash(
            isFirstLaunchLoadingController = viewModel.isFirstLaunchLoadingController,
            healthPointsLoadingController = viewModel.healthPointsLoadingController,
            onGoToHeroCreation = {
                navigator replaceAll HeroCreationScreen()
            },
            onGoToHeroDeath = {
                navigator replaceAll HeroDeathScreen()
            },
            onGoToDashboard = {
                navigator replaceAll DashboardScreen()
            },
        )
    }
}