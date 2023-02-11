package hardcoder.dev.healther.ui.screens.welcome

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.healther.R
import hardcoder.dev.healther.ui.UserViewModel
import hardcoder.dev.healther.ui.base.LocalPresentationModule
import hardcoder.dev.healther.ui.base.composables.IconTextButton
import hardcoder.dev.healther.ui.base.composables.ScaffoldWrapper

@Composable
fun WhereWeGo(onStart: () -> Unit, onSkip: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val userViewModel = viewModel {
        presentationModule.createUserViewModel()
    }
    val state = userViewModel.userData.collectAsState()

    when (val fetchingState = state.value) {
        is UserViewModel.FetchingState.Loaded -> {
            LaunchedEffect(key1 = Unit) {
                if (!fetchingState.userState.isFirstLaunch) {
                    onSkip()
                } else {
                    onStart()
                }
            }
        }

        is UserViewModel.FetchingState.Loading -> {
            /* no-op */
        }
    }
}

@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    ScaffoldWrapper(
        titleResId = R.string.welcome_label,
        content = {
            WelcomeContent(onStart = onStart)
        },
        onGoBack = null
    )
}

@Composable
fun WelcomeContent(onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Column(Modifier.weight(2f)) {
            Text(
                text = stringResource(id = R.string.before_we_start_label),
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        IconTextButton(
            labelResId = R.string.lets_start,
            iconResourceId = Icons.Default.PlayArrow,
            onClick = onStart
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(widthDp = 300, heightDp = 500)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(onStart = {})
}