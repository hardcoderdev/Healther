package hardcoder.dev.androidApp.ui.screens.welcome

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButton
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButtonConfig
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R

@Composable
fun Welcome(onStart: () -> Unit) {
    ScaffoldWrapper(
        content = { WelcomeContent(onStart = onStart) },
        topBarConfig = TopBarConfig(
            type = TopBarType.TitleTopBar(titleResId = R.string.hero_welcome_title_topBar),
        ),
    )
}

@Composable
private fun WelcomeContent(onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        Column(Modifier.weight(2f)) {
            Title(text = stringResource(id = R.string.hero_welcome_beforeWeStart_text))
        }
        Spacer(modifier = Modifier.height(32.dp))
        TextIconButton(
            textIconButtonConfig = TextIconButtonConfig.Filled(
                labelResId = R.string.hero_welcome_letsStart_buttonText,
                iconResId = R.drawable.ic_play,
                onClick = onStart,
            ),
        )
    }
}