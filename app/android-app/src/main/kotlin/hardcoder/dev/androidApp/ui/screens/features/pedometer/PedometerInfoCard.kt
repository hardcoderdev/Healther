package hardcoder.dev.androidApp.ui.screens.features.pedometer

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.text.Label
import hardcoder.dev.uikit.components.text.Title

@Composable
fun PedometerInfoCard(
    @DrawableRes iconResId: Int,
    @StringRes nameResId: Int,
    value: String,
) {
    Column {
        Icon(iconResId = iconResId, contentDescription = stringResource(id = nameResId))
        Spacer(modifier = Modifier.height(8.dp))
        Title(text = value)
        Label(text = stringResource(id = nameResId))
    }
}