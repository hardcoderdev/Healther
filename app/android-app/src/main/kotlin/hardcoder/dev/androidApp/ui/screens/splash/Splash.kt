package hardcoder.dev.androidApp.ui.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import hardcoder.dev.presentation.splash.SplashViewModel
import hardcoder.dev.uikit.components.container.LoadingContainer
import org.koin.androidx.compose.koinViewModel

@Composable
fun Splash(
    onGoToHeroCreation: () -> Unit,
    onGoToHeroDeath: () -> Unit,
    onGoToDashboard: () -> Unit,
    viewModel: SplashViewModel = koinViewModel(),
) {
    LoadingContainer(
        controller1 = viewModel.isFirstLaunchLoadingController,
        controller2 = viewModel.healthPointsLoadingController,
        loadedContent = { isFirstLaunch, healthPoints ->
            LaunchedEffect(isFirstLaunch) {
                when {
                    isFirstLaunch -> {
                        onGoToHeroCreation()
                    }
                    healthPoints <= 0 -> {
                        onGoToHeroDeath()
                    }
                    else -> {
                        onGoToDashboard()
                    }
                }
            }
        },
    )
}