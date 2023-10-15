package hardcoder.dev.uikit.components.tooltip.internal

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.RichTooltipBox
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.text.Label
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPhonePreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.app.ui.resources.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RichTooltip(
    modifier: Modifier = Modifier,
    @StringRes titleResId: Int,
    @StringRes descriptionResId: Int,
    @StringRes actionResId: Int,
    content: @Composable (TooltipBoxScope.() -> Unit),
    onAction: () -> Unit,
) {
    RichTooltipBox(
        modifier = modifier,
        title = { Label(text = stringResource(titleResId)) },
        text = { Label(text = stringResource(descriptionResId)) },
        content = content,
        action = {
            TextButton(onClick = onAction) {
                Label(text = stringResource(actionResId))
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@HealtherUiKitPhonePreview
@Composable
internal fun RichTooltipPreview() {
    HealtherThemePreview {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            RichTooltip(
                titleResId = R.string.timePickerDialog_title_text,
                descriptionResId = R.string.analytics_nowEmpty_text,
                actionResId = R.string.tracking_createEntry_buttonText,
                onAction = {},
                content = {
                    LargeFloatingActionButton(
                        modifier = Modifier.tooltipAnchor(),
                        onClick = {},
                    ) {
                        Icon(
                            iconResId = R.drawable.ic_create,
                            contentDescription = null,
                        )
                    }
                },
            )
        }
    }
}