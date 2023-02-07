package hardcoder.dev.healther.ui.screens.welcome

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hardcoder.dev.healther.R
import hardcoder.dev.healther.ui.base.composables.IconTextButton
import hardcoder.dev.healther.ui.base.composables.ScaffoldWrapper

@Composable
fun WelcomeScreen(onStart: () -> Unit, onSkip: () -> Unit) {
    ScaffoldWrapper(
        titleResId = R.string.welcome_label,
        content = {
            WelcomeContent(
                onStart = onStart,
                onSkip = onSkip
            )
        },
        onGoBack = null
    )
}

@Composable
fun WelcomeContent(onStart: () -> Unit, onSkip: () -> Unit) {
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
    WelcomeScreen(onSkip = {}, onStart = {})
}