package hardcoder.dev.androidApp.ui.screens.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.coroutines.DefaultBackgroundBackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.uikit.components.dialog.TitleDialog
import hardcoder.dev.uikit.components.picker.TimeNumberPicker
import hardcoderdev.healther.app.android.app.R
import kotlinx.datetime.LocalTime

@Composable
fun TimePickerDialog(
    dialogOpen: Boolean,
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
            onUpdateDialogOpen(false)
        },
    )
}

@Preview
@Composable
fun TimePickerDialogPreview() {
    TimePickerDialog(
        onUpdateDialogOpen = {},
        dialogOpen = true,
        timeInputController = InputController(
            rememberCoroutineScope(),
            DateTimeProvider(DefaultBackgroundBackgroundCoroutineDispatchers).currentTime().time,
        ),
    )
}