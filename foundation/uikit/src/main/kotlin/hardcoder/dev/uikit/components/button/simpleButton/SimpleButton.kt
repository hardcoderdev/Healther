package hardcoder.dev.uikit.components.button.simpleButton

import androidx.compose.runtime.Composable
import hardcoder.dev.uikit.components.button.internal.simpleButton.FilledSimpleButton
import hardcoder.dev.uikit.components.button.internal.simpleButton.FilledSimpleButtonPreview
import hardcoder.dev.uikit.components.button.internal.simpleButton.OutlinedSimpleButton
import hardcoder.dev.uikit.components.button.internal.simpleButton.OutlinedSimpleButtonPreview
import hardcoder.dev.uikit.preview.UiKitPreview

@Composable
fun SimpleButton(simpleButtonConfig: SimpleButtonConfig) {
    when (simpleButtonConfig) {
        is SimpleButtonConfig.Filled -> {
            FilledSimpleButton(
                onClick = simpleButtonConfig.onClick,
                modifier = simpleButtonConfig.modifier,
                labelResId = simpleButtonConfig.labelResId,
                isEnabled = simpleButtonConfig.isEnabled,
            )
        }

        is SimpleButtonConfig.Outlined -> {
            OutlinedSimpleButton(
                onClick = simpleButtonConfig.onClick,
                modifier = simpleButtonConfig.modifier,
                labelResId = simpleButtonConfig.labelResId,
                isEnabled = simpleButtonConfig.isEnabled,
            )
        }
    }
}

@UiKitPreview
@Composable
private fun FilledSimpleButtonPreview() {
    FilledSimpleButtonPreview()
}

@UiKitPreview
@Composable
private fun OutlinedSimpleButtonPreview() {
    OutlinedSimpleButtonPreview()
}