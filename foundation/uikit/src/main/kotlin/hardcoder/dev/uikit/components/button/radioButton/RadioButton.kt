package hardcoder.dev.uikit.components.button.radioButton

import androidx.compose.runtime.Composable
import hardcoder.dev.uikit.components.button.internal.radioButton.FilledRadioButton
import hardcoder.dev.uikit.components.button.internal.radioButton.FilledRadioButtonPreview
import hardcoder.dev.uikit.components.button.internal.radioButton.OutlinedRadioButton
import hardcoder.dev.uikit.components.button.internal.radioButton.OutlinedRadioButtonPreview
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview

@Composable
fun RadioButton(radioButtonConfig: RadioButtonConfig) {
    when (radioButtonConfig) {
        is RadioButtonConfig.Filled -> FilledRadioButton(
            labelResId = radioButtonConfig.labelResId,
            onClick = radioButtonConfig.onClick,
            isSelected = radioButtonConfig.isSelected,
            modifier = radioButtonConfig.modifier,
            isEnabled = radioButtonConfig.isEnabled,
        )
        is RadioButtonConfig.Outlined -> OutlinedRadioButton(
            labelResId = radioButtonConfig.labelResId,
            onClick = radioButtonConfig.onClick,
            isSelected = radioButtonConfig.isSelected,
            modifier = radioButtonConfig.modifier,
            isEnabled = radioButtonConfig.isEnabled,
        )
    }
}

@HealtherUiKitPreview
@Composable
private fun FilledRadioButtonPreview() {
    FilledRadioButtonPreview()
}

@HealtherUiKitPreview
@Composable
private fun OutlinedRadioButtonPreview() {
    OutlinedRadioButtonPreview()
}