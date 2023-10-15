package hardcoder.dev.uikit.components.icon

import androidx.annotation.DrawableRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R
import androidx.compose.material.Icon as MaterialIcon

@Composable
fun Icon(
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int,
    contentDescription: String? = null,
) {
    MaterialIcon(
        modifier = modifier,
        painter = painterResource(id = iconResId),
        contentDescription = contentDescription,
        tint = MaterialTheme.colorScheme.onBackground,
    )
}

@HealtherUiKitPreview
@Composable
private fun IconPreview() {
    HealtherThemePreview {
        Icon(
            iconResId = R.drawable.ic_add,
        )
    }
}