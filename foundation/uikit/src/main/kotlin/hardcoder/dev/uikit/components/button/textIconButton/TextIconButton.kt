package hardcoder.dev.uikit.components.button.textIconButton

import androidx.compose.runtime.Composable
import hardcoder.dev.uikit.components.button.internal.textIconButton.FilledTextIconButton
import hardcoder.dev.uikit.components.button.internal.textIconButton.FilledTextIconButtonPreview
import hardcoder.dev.uikit.components.button.internal.textIconButton.OutlinedTextIconButton
import hardcoder.dev.uikit.components.button.internal.textIconButton.OutlinedTextIconButtonPreview
import hardcoder.dev.uikit.preview.UiKitPreview

@Composable
fun TextIconButton(textIconButtonConfig: TextIconButtonConfig) {
    when (textIconButtonConfig) {
        is TextIconButtonConfig.Filled -> {
            FilledTextIconButton(
                modifier = textIconButtonConfig.modifier,
                onClick = textIconButtonConfig.onClick,
                isEnabled = textIconButtonConfig.isEnabled,
                labelResId = textIconButtonConfig.labelResId,
                iconResId = textIconButtonConfig.iconResId,
                formatArgs = textIconButtonConfig.formatArgs,
                contentDescription = textIconButtonConfig.contentDescription,
            )
        }

        is TextIconButtonConfig.Outlined -> {
            OutlinedTextIconButton(
                modifier = textIconButtonConfig.modifier,
                onClick = textIconButtonConfig.onClick,
                isEnabled = textIconButtonConfig.isEnabled,
                labelResId = textIconButtonConfig.labelResId,
                iconResId = textIconButtonConfig.iconResId,
                formatArgs = textIconButtonConfig.formatArgs,
                contentDescription = textIconButtonConfig.contentDescription,
            )
        }
    }
}

@UiKitPreview
@Composable
private fun FilledTextIconButtonPreview() {
    FilledTextIconButtonPreview()
}

@UiKitPreview
@Composable
private fun OutlinedTextIconButtonPreview() {
    OutlinedTextIconButtonPreview()
}