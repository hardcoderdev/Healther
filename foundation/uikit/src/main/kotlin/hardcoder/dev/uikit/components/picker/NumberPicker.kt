package hardcoder.dev.uikit.components.picker

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import com.chargemap.compose.numberpicker.NumberPicker as LibraryNumberPicker

@Composable
fun NumberPicker(
    modifier: Modifier = Modifier,
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
) {
    LibraryNumberPicker(
        modifier = modifier.fillMaxWidth(),
        value = value,
        range = range,
        dividersColor = MaterialTheme.colorScheme.primary,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.onBackground,
        ),
    )
}

@Composable
fun NumberInput(
    modifier: Modifier = Modifier,
    controller: InputController<Int>,
    range: IntRange,
) {
    val state by controller.state.collectAsState()

    NumberPicker(
        modifier = modifier,
        value = state.input,
        range = range,
        onValueChange = controller::changeInput,
    )
}

@HealtherUiKitPreview
@Composable
private fun NumberPickerPreview() {
    var selectedNumber by remember {
        mutableStateOf(0)
    }

    HealtherThemePreview {
        NumberPicker(
            value = selectedNumber,
            range = 1..100,
            onValueChange = {
                selectedNumber = it
            },
        )
    }
}