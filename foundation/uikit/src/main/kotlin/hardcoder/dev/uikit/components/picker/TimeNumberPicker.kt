package hardcoder.dev.uikit.components.picker

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.Hours
import com.chargemap.compose.numberpicker.HoursNumberPicker
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.uikit.preview.UiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import kotlinx.datetime.LocalTime

private fun LocalTime.toFullHours() = FullHours(
    hours = hour,
    minutes = minute,
)

private fun Hours.toLocalTime() = LocalTime(
    hour = hours,
    minute = minutes,
)

@Composable
fun TimeNumberPicker(
    modifier: Modifier = Modifier,
    controller: InputController<LocalTime>,
) {
    val state by controller.state.collectAsState()

    HoursNumberPicker(
        modifier = modifier.fillMaxWidth(),
        dividersColor = MaterialTheme.colorScheme.primary,
        textStyle = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.onBackground,
        ),
        value = state.input.toFullHours(),
        onValueChange = {
            controller.changeInput(it.toLocalTime())
        },
    )
}

@UiKitPreview
@Composable
private fun TimeNumberPickerPreview() {
    HealtherThemePreview {
        TimeNumberPicker(
            controller = InputController(
                coroutineScope = rememberCoroutineScope(),
                initialInput = LocalTime(0, 0),
            ),
        )
    }
}