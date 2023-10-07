package hardcoder.dev.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.uikit.components.container.LoadingContainer

@Composable
fun Splash(
    onGoToHeroCreation: () -> Unit,
    onGoToDashboard: () -> Unit,
    isFirstLaunchLoadingController: LoadingController<Boolean>,
) {
    LoadingContainer(
        controller = isFirstLaunchLoadingController,
        loadedContent = { isFirstLaunch ->
            LaunchedEffect(isFirstLaunch) {
                if (isFirstLaunch) {
                    onGoToHeroCreation()
                } else {
                    onGoToDashboard()
                }
            }
        },
    )
}