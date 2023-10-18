package hardcoder.dev.navigation.screens.mainFlow.splash

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.splash.SplashViewModel
import hardcoder.dev.screens.splash.Splash
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SplashScreen(navController: NavController) {
    val viewModel = koinViewModel<SplashViewModel>()

    Splash(
        isFirstLaunchLoadingController = viewModel.isFirstLaunchLoadingController,
        onGoToHeroCreation = {
            navController.navigate(Screen.UserCreation.route)
        },
        onGoToDashboard = {
            navController.navigate(Screen.Dashboard.route)
        },
    )
}