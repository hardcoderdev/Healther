package hardcoder.dev.uikit.components.button.internal.circleIconButton

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R
import androidx.compose.material3.Icon as MaterialIcon
import androidx.compose.material3.IconButton as MaterialIconButton

@Composable
internal fun FilledCircleIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes iconResId: Int,
    @StringRes contentDescription: Int?,
) {
    MaterialIconButton(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(16.dp),
        ),
        onClick = onClick,
    ) {
        MaterialIcon(
            modifier = Modifier
                .padding(8.dp),
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription?.let { stringResource(id = it) },
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@HealtherUiKitPreview
@Composable
internal fun FilledCircleIconButtonPreview() {
    HealtherThemePreview {
        FilledCircleIconButton(
            onClick = { /* no-op */ },
            iconResId = R.drawable.ic_clear,
            contentDescription = null,
        )
    }
}