package hardcoder.dev.androidApp.ui.setUpFlow.welcome

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.ButtonWithIcon
import hardcoder.dev.uikit.text.Title
import hardcoderdev.healther.app.android.app.R

@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    ScaffoldWrapper(
        content = { WelcomeContent(onStart = onStart) },
        topBarConfig = TopBarConfig(
            type = TopBarType.TitleTopBar(titleResId = R.string.setUpFlow_welcome_title_topBar)
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
            Title(text = stringResource(id = R.string.setUpFlow_welcome_beforeWeStart_text))
        }
        Spacer(modifier = Modifier.height(32.dp))
        ButtonWithIcon(
            labelResId = R.string.setUpFlow_welcome_letsStart_buttonText,
            iconResId = R.drawable.ic_play,
            onClick = onStart
        )
    }
}