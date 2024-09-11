package hardcoder.dev.uikit.sections.dateTime

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hardcoder.dev.blocks.components.button.textIconButton.TextIconButton
import hardcoder.dev.blocks.components.button.textIconButton.TextIconButtonConfig
import hardcoder.dev.blocks.components.text.Title
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.coroutines.DefaultBackgroundBackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.uikit.components.dialog.DatePickerDialog
import hardcoder.dev.uikit.components.dialog.TimePickerDialog
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.app.ui.resources.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Composable
fun DateTimeSection(
    dateTimeProvider: DateTimeProvider,
    dateTimeFormatter: DateTimeFormatter,
    dateInputController: InputController<LocalDate>,
    timeInputController: InputController<LocalTime>,
) {
    val dateInputControllerState by dateInputController.state.collectAsState()
    val formattedDate = dateTimeFormatter.formatDate(dateInputControllerState.input)
    var isDateDialogOpen by remember {
        mutableStateOf(false)
    }

    val timeInputControllerState by timeInputController.state.collectAsState()
    val formattedTime = dateTimeFormatter.formatTime(timeInputControllerState.input)
    var isTimeDialogOpen by remember {
        mutableStateOf(false)
    }

    Title(text = stringResource(id = R.string.dateTime_selectDateAndTime))
    Spacer(modifier = Modifier.height(16.dp))
    TextIconButton(
        textIconButtonConfig = TextIconButtonConfig.Outlined(
            iconResId = R.drawable.ic_date,
            labelResId = R.string.dateTime_selectedDate_formatText,
            formatArgs = listOf(formattedDate),
            onClick = {
                isDateDialogOpen = true
            },
        ),
    )

    DatePickerDialog(
        dateTimeProvider = dateTimeProvider,
        dialogOpen = isDateDialogOpen,
        onUpdateDialogOpen = { isDateDialogOpen = it },
        dateInputController = dateInputController,
    )

    Spacer(modifier = Modifier.height(16.dp))
    TextIconButton(
        textIconButtonConfig = TextIconButtonConfig.Outlined(
            iconResId = R.drawable.ic_time,
            labelResId = R.string.dateTime_selectedTime_formatText,
            formatArgs = listOf(formattedTime),
            onClick = {
                isTimeDialogOpen = true
            },
        ),
    )

    TimePickerDialog(
        dialogOpen = isTimeDialogOpen,
        dateTimeProvider = dateTimeProvider,
        onUpdateDialogOpen = { isTimeDialogOpen = it },
        timeInputController = timeInputController,
    )
}

@Preview
@Composable
private fun DateTimeSectionPreview() {
    HealtherThemePreview {
        DateTimeSection(
            dateTimeFormatter = DateTimeFormatter(context = LocalContext.current),
            dateTimeProvider = DateTimeProvider(dispatchers = DefaultBackgroundBackgroundCoroutineDispatchers),
            dateInputController = MockControllersProvider.inputController(MockDateProvider.localDate()),
            timeInputController = MockControllersProvider.inputController(MockDateProvider.localTime()),
        )
    }
}