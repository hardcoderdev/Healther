package hardcoder.dev.uikit.components.button.circleIconButton

import androidx.compose.runtime.Composable
import hardcoder.dev.uikit.components.button.internal.circleIconButton.FilledCircleIconButton
import hardcoder.dev.uikit.components.button.internal.circleIconButton.FilledCircleIconButtonPreview
import hardcoder.dev.uikit.components.button.internal.circleIconButton.OutlinedCircleIconButton
import hardcoder.dev.uikit.components.button.internal.circleIconButton.OutlinedCircleIconButtonPreview
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview

@Composable
fun CircleIconButton(circleIconButtonConfig: CircleIconButtonConfig) {
    when (circleIconButtonConfig) {
        is CircleIconButtonConfig.Filled -> {
            FilledCircleIconButton(
                modifier = circleIconButtonConfig.modifier,
                onClick = circleIconButtonConfig.onClick,
                iconResId = circleIconButtonConfig.iconResId,
                contentDescription = circleIconButtonConfig.contentDescription,
            )
        }

        is CircleIconButtonConfig.Outlined -> {
            OutlinedCircleIconButton(
                modifier = circleIconButtonConfig.modifier,
                onClick = circleIconButtonConfig.onClick,
                iconResId = circleIconButtonConfig.iconResId,
                contentDescription = circleIconButtonConfig.contentDescription,
            )
        }
    }
}

@HealtherUiKitPreview
@Composable
private fun FilledCircleIconButtonPreview() {
    FilledCircleIconButtonPreview()
}

@HealtherUiKitPreview
@Composable
private fun OutlinedCircleIconButtonPreview() {
    OutlinedCircleIconButtonPreview()
}