package hardcoder.dev.androidApp.ui.setUpFlow.welcome

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hardcoder.dev.healther.R
import hardcoder.dev.uikit.IconTextButton
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType

@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    ScaffoldWrapper(
        content = { WelcomeContent(onStart = onStart) },
        topBarConfig = TopBarConfig(
            type = TopBarType.TitleTopBar(titleResId = R.string.welcome_title_topBar)
        )
    )
}

@Composable
private fun WelcomeContent(onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Column(Modifier.weight(2f)) {
            Text(
                text = stringResource(id = R.string.welcome_beforeWeStart_text),
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        IconTextButton(
            labelResId = R.string.welcome_letsStart_button,
            imageVector = Icons.Default.PlayArrow,
            onClick = onStart,
            contentDescription = R.string.welcome_start_iconContentDescription
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(widthDp = 300, heightDp = 500)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(onStart = {})
}