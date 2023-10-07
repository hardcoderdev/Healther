package hardcoder.dev.uikit.components.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.coroutines.DefaultBackgroundBackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.uikit.components.picker.TimeNumberPicker
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R
import kotlinx.datetime.LocalTime

@Composable
fun TimePickerDialog(
    dialogOpen: Boolean,
    dateTimeProvider: DateTimeProvider,
    timeInputController: InputController<LocalTime>,
    onUpdateDialogOpen: (Boolean) -> Unit,
) {
    TitleDialog(
        dialogOpen = dialogOpen,
        onUpdateDialogOpen = onUpdateDialogOpen,
        value = timeInputController.getInput(),
        iconResId = R.drawable.ic_time,
        titleResId = R.string.timePickerDialog_title_text,
        negativeOptionResId = R.string.timePickerDialog_negative_option,
        positiveOptionResId = R.string.timePickerDialog_positive_option,
        dialogContent = {
            TimeNumberPicker(controller = timeInputController)
        },
        onSelect = {
            onUpdateDialogOpen(false)
        },
        onCancel = {
            timeInputController.changeInput(dateTimeProvider.currentTime().time)
            onUpdateDialogOpen(false)
        },
    )
}

@Preview
@Composable
private fun TimePickerDialogPreview() {
    HealtherTheme {
        TimePickerDialog(
            onUpdateDialogOpen = {},
            dialogOpen = true,
            dateTimeProvider = DateTimeProvider(dispatchers = DefaultBackgroundBackgroundCoroutineDispatchers),
            timeInputController = MockControllersProvider.inputController(MockDateProvider.localTime()),
        )
    }
}