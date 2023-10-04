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
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.coroutines.DefaultBackgroundBackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButton
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButtonConfig
import hardcoder.dev.uikit.components.dialog.DatePickerDialog
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.app.resources.R
import kotlinx.datetime.LocalDate

@Composable
fun DateSection(
    dateTimeProvider: DateTimeProvider,
    dateTimeFormatter: DateTimeFormatter,
    dateInputController: InputController<LocalDate>,
) {
    val dateInputControllerState by dateInputController.state.collectAsState()
    val formattedDate = dateTimeFormatter.formatDate(dateInputControllerState.input)
    var dialogOpen by remember {
        mutableStateOf(false)
    }

    Title(text = stringResource(id = R.string.dateTime_selectDate))
    Spacer(modifier = Modifier.height(16.dp))
    TextIconButton(
        textIconButtonConfig = TextIconButtonConfig.Outlined(
            iconResId = R.drawable.ic_date,
            labelResId = R.string.dateTime_selectedDate_formatText,
            formatArgs = listOf(formattedDate),
            onClick = {
                dialogOpen = true
            },
        ),
    )

    DatePickerDialog(
        dateTimeProvider = dateTimeProvider,
        dialogOpen = dialogOpen,
        onUpdateDialogOpen = { dialogOpen = it },
        dateInputController = dateInputController,
    )
}

@Preview
@Composable
private fun DateSectionPreview() {
    HealtherThemePreview {
        DateSection(
            dateInputController = MockControllersProvider.inputController(MockDateProvider.localDate()),
            dateTimeFormatter = DateTimeFormatter(context = LocalContext.current),
            dateTimeProvider = DateTimeProvider(dispatchers = DefaultBackgroundBackgroundCoroutineDispatchers),
        )
    }
}