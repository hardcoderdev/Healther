package hardcoder.dev.uikit.components.tooltip.internal

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.TooltipBoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.text.Label
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPhonePreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlainTooltip(
    modifier: Modifier = Modifier,
    @StringRes tooltipResId: Int,
    content: @Composable (TooltipBoxScope.() -> Unit),
) {
    PlainTooltipBox(
        modifier = modifier,
        tooltip = { Label(text = stringResource(tooltipResId)) },
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@HealtherUiKitPhonePreview
@Composable
internal fun PlainTooltipPreview() {
    HealtherThemePreview {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            PlainTooltip(
                tooltipResId = R.string.default_nowEmpty_text,
                content = {
                    LargeFloatingActionButton(
                        modifier = Modifier.tooltipAnchor(),
                        onClick = {},
                    ) {
                        Icon(
                            iconResId = R.drawable.ic_add,
                            contentDescription = null,
                        )
                    }
                },
            )
        }
    }
}