package hardcoder.dev.androidApp.ui.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import hardcoder.dev.presentation.welcome.SplashViewModel
import hardcoder.dev.uikit.components.container.LoadingContainer
import org.koin.androidx.compose.koinViewModel

@Composable
fun Splash(
    onStartSetUp: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    viewModel: SplashViewModel = koinViewModel(),
) {
    LoadingContainer(
        controller = viewModel.isFirstLaunchLoadingController,
        loadedContent = { isFirstLaunch ->
            LaunchedEffect(isFirstLaunch) {
                if (isFirstLaunch) {
                    onStartSetUp()
                } else {
                    onNavigateToDashboard()
                }
            }
        },
    )
}