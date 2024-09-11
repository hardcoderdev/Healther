package hardcoder.dev.screens.features.pedometer

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hardcoder.dev.blocks.components.icon.Icon
import hardcoder.dev.blocks.components.text.Label
import hardcoder.dev.blocks.components.text.Title
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

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

@Preview
@Composable
private fun PedometerInfoCardPreview() {
    HealtherTheme {
        PedometerInfoCard(
            iconResId = R.drawable.ic_directions_walk,
            nameResId = R.string.pedometer_stepsLabel_text,
            value = "13 000",
        )
    }
}