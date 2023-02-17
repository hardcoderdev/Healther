package hardcoder.dev.healther.ui.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.healther.ui.base.LocalPresentationModule

@Composable
fun SplashScreen(onStartSetUp: () -> Unit, onNavigateToDashboard: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val splashViewModel = viewModel {
        presentationModule.createSplashViewModel()
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