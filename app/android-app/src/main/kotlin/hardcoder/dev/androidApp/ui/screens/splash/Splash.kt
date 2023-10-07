package hardcoder.dev.androidApp.ui.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.uikit.components.container.LoadingContainer

@Composable
fun Splash(
    onGoToHeroCreation: () -> Unit,
    onGoToHeroDeath: () -> Unit,
    onGoToDashboard: () -> Unit,
    isFirstLaunchLoadingController: LoadingController<Boolean>,
    healthPointsLoadingController: LoadingController<Int>,
) {
    LoadingContainer(
        controller1 = isFirstLaunchLoadingController,
        controller2 = healthPointsLoadingController,
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