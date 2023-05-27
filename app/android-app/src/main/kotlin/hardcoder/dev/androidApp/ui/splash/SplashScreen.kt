package hardcoder.dev.androidApp.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.uikit.LoadingContainer

@Composable
fun SplashScreen(onStartSetUp: () -> Unit, onNavigateToDashboard: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val splashViewModel = viewModel {
        presentationModule.getSplashViewModel()
    }

    LoadingContainer(
        controller = splashViewModel.isFirstLaunchLoadingController,
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