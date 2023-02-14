package hardcoder.dev.healther.ui.screens.launch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.healther.ui.base.LocalPresentationModule

@Composable
fun SplashScreen(onStart: () -> Unit, onSkip: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val splashViewModel = viewModel {
        presentationModule.createSplashViewModel()
    }
    val state = splashViewModel.state.collectAsState()

    var splashVisibility by remember {
        mutableStateOf(false)
    }

    when (val fetchingState = state.value) {
        is SplashViewModel.FetchingState.Loaded -> {
            LaunchedEffect(key1 = Unit) {
                if (!fetchingState.state.isFirstLaunch) {
                    onSkip()
                } else {
                    onStart()
                }
            }
        }

        is SplashViewModel.FetchingState.Loading -> {
            /* no-op */
        }
    }
}