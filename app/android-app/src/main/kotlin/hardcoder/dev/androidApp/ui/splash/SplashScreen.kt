package hardcoder.dev.androidApp.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.ui.LocalPresentationModule
import hardcoder.dev.presentation.setUpFlow.SplashViewModel

@Composable
fun SplashScreen(onStartSetUp: () -> Unit, onNavigateToDashboard: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val splashViewModel = viewModel {
        presentationModule.getSplashViewModel()
    }
    val state = splashViewModel.state.collectAsState()

    LaunchedEffect(key1 = state.value) {
        when (val fetchingState = state.value) {
            is SplashViewModel.FetchingState.Loaded -> {
                if (!fetchingState.state.isFirstLaunch) {
                    onNavigateToDashboard()
                } else {
                    onStartSetUp()
                }
            }

            is SplashViewModel.FetchingState.Loading -> {
                /* no-op */
            }
        }
    }
}