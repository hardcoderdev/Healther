package hardcoder.dev.uikit.components.button.internal.managementButton

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.components.chip.Chip
import hardcoder.dev.uikit.components.chip.ChipConfig
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
internal fun ChipManagementButton(
    modifier: Modifier = Modifier,
    @StringRes titleResId: Int,
    @DrawableRes iconResId: Int = R.drawable.ic_create,
    onManage: () -> Unit,
) {
    Chip(
        chipConfig = ChipConfig.Action(
            modifier = modifier.padding(4.dp),
            onClick = onManage,
            text = stringResource(id = titleResId),
            iconResId = iconResId,
            shape = RoundedCornerShape(32.dp),
        ),
    )
}

@HealtherUiKitPreview
@Composable
internal fun ChipManagementButtonPreview() {
    HealtherTheme {
        ChipManagementButton(
            onManage = {},
            titleResId = R.string.diary_creation_title_topBar,
        )
    }
}