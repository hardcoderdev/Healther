package hardcoder.dev.uikit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NumberPicker(
    modifier: Modifier = Modifier,
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    com.chargemap.compose.numberpicker.NumberPicker(
        modifier = modifier.fillMaxWidth(),
        value = value,
        range = range,
        dividersColor = MaterialTheme.colorScheme.primary,
        textStyle = MaterialTheme.typography.titleMedium,
        onValueChange = onValueChange
    )
}