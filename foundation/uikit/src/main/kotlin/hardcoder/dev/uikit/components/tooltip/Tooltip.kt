@file:OptIn(ExperimentalMaterial3Api::class)

package hardcoder.dev.uikit.components.tooltip

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import hardcoder.dev.uikit.components.tooltip.internal.PlainTooltip
import hardcoder.dev.uikit.components.tooltip.internal.PlainTooltipPreview
import hardcoder.dev.uikit.components.tooltip.internal.RichTooltip
import hardcoder.dev.uikit.components.tooltip.internal.RichTooltipPreview
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview

@Composable
fun Tooltip(tooltipConfig: TooltipConfig) {
    when (tooltipConfig) {
        is TooltipConfig.Plain -> {
            PlainTooltip(
                modifier = tooltipConfig.modifier,
                tooltipResId = tooltipConfig.tooltipResId,
                content = tooltipConfig.content,
            )
        }

        is TooltipConfig.Rich -> {
            RichTooltip(
                modifier = tooltipConfig.modifier,
                titleResId = tooltipConfig.titleResId,
                descriptionResId = tooltipConfig.descriptionResId,
                actionResId = tooltipConfig.actionResId,
                onAction = tooltipConfig.onAction,
                content = tooltipConfig.content,
            )
        }
    }
}

@HealtherUiKitPreview
@Composable
private fun PlainTooltipPreview() {
    PlainTooltipPreview()
}

@HealtherUiKitPreview
@Composable
private fun RichTooltipPreview() {
    RichTooltipPreview()
}
