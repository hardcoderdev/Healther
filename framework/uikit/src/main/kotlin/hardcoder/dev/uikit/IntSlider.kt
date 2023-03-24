package hardcoder.dev.uikit

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun IntSlider(
    selectedValue: Int,
    valueRange: IntRange,
    onValueChange: (Int) -> Unit,
    enabled: Boolean = true,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Slider(
        value = selectedValue.toFloat(),
        onValueChange = { onValueChange(it.toInt()) },
        valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
        colors = SliderDefaults.colors(
            inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer
        ),
        enabled = enabled,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        interactionSource = interactionSource
    )
}