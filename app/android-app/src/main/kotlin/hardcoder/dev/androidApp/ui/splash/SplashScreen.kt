package hardcoder.dev.androidApp.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import hardcoder.dev.presentation.setUpFlow.SplashViewModel
import hardcoder.dev.uikit.LoadingContainer
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    onStartSetUp: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    val viewModel = koinViewModel<SplashViewModel>()

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
        }
    )
}