package hardcoder.dev.uikit.components.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.coroutines.DefaultBackgroundBackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.uikit.components.calendar.SingleSelectionCalendar
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R
import kotlinx.datetime.LocalDate

@Composable
fun DatePickerDialog(
    dateTimeProvider: DateTimeProvider,
    dialogOpen: Boolean,
    dateInputController: InputController<LocalDate>,
    onUpdateDialogOpen: (Boolean) -> Unit,
) {
    TitleDialog(
        dialogOpen = dialogOpen,
        onUpdateDialogOpen = onUpdateDialogOpen,
        value = dateInputController.getInput(),
        iconResId = R.drawable.ic_date,
        titleResId = R.string.datePickerDialog_title_text,
        negativeOptionResId = R.string.datePickerDialog_negative_option,
        positiveOptionResId = R.string.datePickerDialog_positive_option,
        onSelect = { onUpdateDialogOpen(false) },
        onCancel = {
            dateInputController.changeInput(dateTimeProvider.currentDate())
            onUpdateDialogOpen(false)
        },
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
private fun DatePickerDialogPreview() {
    HealtherTheme {
        DatePickerDialog(
            dateTimeProvider = DateTimeProvider(dispatchers = DefaultBackgroundBackgroundCoroutineDispatchers),
            dateInputController = MockControllersProvider.inputController(MockDateProvider.localDate()),
            onUpdateDialogOpen = {},
            dialogOpen = true,
        )
    }
}