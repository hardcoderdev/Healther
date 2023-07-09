package hardcoder.dev.androidApp.ui.screens.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.uikit.components.calendar.SingleSelectionCalendar
import hardcoder.dev.uikit.components.dialog.TitleDialog
import hardcoderdev.healther.app.android.app.R
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject

@Composable
fun DatePickerDialog(
    dialogOpen: Boolean,
    dateInputController: InputController<LocalDate>,
    onUpdateDialogOpen: (Boolean) -> Unit,
) {
    val dateTimeProvider = koinInject<DateTimeProvider>()

    TitleDialog(
        dialogOpen = dialogOpen,
        onUpdateDialogOpen = onUpdateDialogOpen,
        value = dateInputController.getInput(),
        iconResId = R.drawable.ic_date,
        titleResId = R.string.datePickerDialog_title_text,
        negativeOptionResId = R.string.datePickerDialog_negative_option,
        positiveOptionResId = R.string.datePickerDialog_positive_option,
        onSelect = { onUpdateDialogOpen(false) },
        onCancel = { onUpdateDialogOpen(false) },
        dialogContent = {
            SingleSelectionCalendar(
                dateTimeProvider = dateTimeProvider,
                inputChanged = { date ->
                    dateInputController.changeInput(date)
                },
            )
        },
    )
}

@Preview
@Composable
fun DatePickerDialogPreview() {
    DatePickerDialog(
        dateInputController = InputController(
            coroutineScope = rememberCoroutineScope(),
            initialInput = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
        ),
        onUpdateDialogOpen = {},
        dialogOpen = true,
    )
}