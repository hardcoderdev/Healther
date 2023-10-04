package hardcoder.dev.androidApp.ui.screens.features.moodTracking.update

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.androidApp.ui.screens.dialogs.DatePickerDialog
import hardcoder.dev.androidApp.ui.screens.dialogs.TimePickerDialog
import hardcoder.dev.androidApp.ui.screens.features.moodTracking.moodType.MoodItem
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.MultiSelectionController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.coroutines.DefaultBackgroundBackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.icons.resourceId
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivity
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.mock.dataProviders.features.MoodTrackingMockDataProvider
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButton
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButtonConfig
import hardcoder.dev.uikit.components.card.Card
import hardcoder.dev.uikit.components.card.CardConfig
import hardcoder.dev.uikit.components.chip.Chip
import hardcoder.dev.uikit.components.chip.ChipConfig
import hardcoder.dev.uikit.components.chip.content.ChipIconDefaultContent
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.list.flowRow.MultiSelectionChipFlowRow
import hardcoder.dev.uikit.components.list.tabRow.SingleCardSelectionScrollableTabRow
import hardcoder.dev.uikit.components.text.ErrorText
import hardcoder.dev.uikit.components.text.Label
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.text.textField.TextField
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Composable
fun MoodTrackingUpdate(
    dateTimeProvider: DateTimeProvider,
    dateTimeFormatter: DateTimeFormatter,
    moodTypeSelectionController: SingleSelectionController<MoodType>,
    activitiesMultiSelectionController: MultiSelectionController<MoodActivity>,
    dateInputController: InputController<LocalDate>,
    timeInputController: InputController<LocalTime>,
    noteInputController: InputController<String>,
    updateController: RequestController,
    onManageMoodTypes: () -> Unit,
    onManageMoodActivities: () -> Unit,
    onGoBack: () -> Unit,
    onDeleteShowDialog: (Boolean) -> Unit,
) {
    ScaffoldWrapper(
        content = {
            MoodTrackingUpdateContent(
                dateTimeProvider = dateTimeProvider,
                dateTimeFormatter = dateTimeFormatter,
                dateInputController = dateInputController,
                timeInputController = timeInputController,
                noteInputController = noteInputController,
                moodTypeSelectionController = moodTypeSelectionController,
                updateController = updateController,
                multiSelectionController = activitiesMultiSelectionController,
                onManageMoodTypes = onManageMoodTypes,
                onManageMoodActivities = onManageMoodActivities,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.tracking_update_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_delete,
                    onActionClick = {
                        onDeleteShowDialog(true)
                    },
                ),
            ),
        ),
    )
}

@Composable
private fun MoodTrackingUpdateContent(
    dateTimeProvider: DateTimeProvider,
    dateTimeFormatter: DateTimeFormatter,
    moodTypeSelectionController: SingleSelectionController<MoodType>,
    multiSelectionController: MultiSelectionController<MoodActivity>,
    dateInputController: InputController<LocalDate>,
    timeInputController: InputController<LocalTime>,
    noteInputController: InputController<String>,
    updateController: RequestController,
    onManageMoodTypes: () -> Unit,
    onManageMoodActivities: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            Modifier
                .weight(2f)
                .verticalScroll(rememberScrollState()),
        ) {
            SelectMoodSection(
                moodTypeSelectionController = moodTypeSelectionController,
                onManageMoodTypes = onManageMoodTypes,
            )
            Spacer(modifier = Modifier.height(16.dp))
            EnterNoteSection(noteInputController = noteInputController)
            Spacer(modifier = Modifier.height(16.dp))
            SelectActivitiesSection(
                multiSelectionController = multiSelectionController,
                onManageActivities = onManageMoodActivities,
            )
            Spacer(modifier = Modifier.height(16.dp))
            SelectDateSection(
                dateTimeProvider = dateTimeProvider,
                dateTimeFormatter = dateTimeFormatter,
                dateInputController = dateInputController,
            )
            Spacer(modifier = Modifier.height(16.dp))
            SelectTimeSection(
                dateTimeFormatter = dateTimeFormatter,
                timeInputController = timeInputController,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            requestButtonConfig = RequestButtonConfig.Filled(
                controller = updateController,
                iconResId = R.drawable.ic_save,
                labelResId = R.string.tracking_updateEntry_buttonText,
            ),
        )
    }
}

@Composable
private fun SelectMoodSection(
    moodTypeSelectionController: SingleSelectionController<MoodType>,
    onManageMoodTypes: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Title(text = stringResource(id = R.string.moodTracking_creation_howYouFeelYourself_text))
        Spacer(modifier = Modifier.height(16.dp))
        SingleCardSelectionScrollableTabRow(
            controller = moodTypeSelectionController,
            actionButton = { MoodTypeManagementButton(onManageMoodTypes = onManageMoodTypes) },
            emptyContent = {
                MoodTypeManagementButton(onManageMoodTypes = onManageMoodTypes)
                Spacer(modifier = Modifier.height(8.dp))
                ErrorText(text = stringResource(id = R.string.moodTracking_creation_moodTypeNotSelected_text))
            },
            itemModifier = {
                Modifier.padding(
                    start = 4.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp,
                )
            },
            itemContent = { moodType, _ ->
                MoodItem(moodType = moodType)
            },
        )
    }
}

@Composable
private fun MoodTypeManagementButton(onManageMoodTypes: () -> Unit) {
    Card(
        cardConfig = CardConfig.Action(
            onClick = onManageMoodTypes,
            modifier = Modifier.padding(
                start = 4.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 16.dp,
            ),
            cardContent = {
                Column(
                    modifier = Modifier
                        .width(130.dp)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        modifier = Modifier.height(60.dp),
                        painter = painterResource(id = R.drawable.ic_create),
                        contentDescription = stringResource(id = R.string.moodTracking_moodType_update_manageMoodTypes_buttonText),
                        alignment = Alignment.Center,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Label(text = stringResource(id = R.string.moodTracking_moodType_update_manageMoodTypes_buttonText))
                }
            },
        ),
    )
}

@Composable
private fun SelectActivitiesSection(
    multiSelectionController: MultiSelectionController<MoodActivity>,
    onManageActivities: () -> Unit,
) {
    Title(text = stringResource(id = R.string.moodTracking_creation_youMaySelectActivities_text))
    Spacer(modifier = Modifier.height(16.dp))
    MultiSelectionChipFlowRow(
        controller = multiSelectionController,
        maxItemsInEachRow = 6,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.Center,
        itemModifier = Modifier.padding(top = 8.dp),
        emptyContent = { ManagementActivitiesButton(onManageActivities = onManageActivities) },
        actionButton = { ManagementActivitiesButton(onManageActivities = onManageActivities) },
        itemContent = { activity, _ ->
            ChipIconDefaultContent(
                iconResId = activity.icon.resourceId,
                name = activity.name,
            )
        },
    )
}

@Composable
private fun ManagementActivitiesButton(onManageActivities: () -> Unit) {
    Chip(
        chipConfig = ChipConfig.Action(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(id = R.string.moodTracking_creation_manageActivities_buttonText),
            iconResId = R.drawable.ic_create,
            shape = RoundedCornerShape(32.dp),
            onClick = onManageActivities,
        ),
    )
}

@Composable
private fun EnterNoteSection(noteInputController: InputController<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Title(text = stringResource(id = R.string.moodTracking_creation_youCanAddNote_text))
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            controller = noteInputController,
            label = R.string.moodTracking_creation_enterNote_textField,
            multiline = true,
            maxLines = 5,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
        )
    }
}

@Composable
private fun SelectDateSection(
    dateTimeProvider: DateTimeProvider,
    dateTimeFormatter: DateTimeFormatter,
    dateInputController: InputController<LocalDate>,
) {
    val dateInputControllerState by dateInputController.state.collectAsState()
    val formattedDate = dateTimeFormatter.formatDate(dateInputControllerState.input)
    var dialogOpen by remember {
        mutableStateOf(false)
    }

    Title(text = stringResource(id = R.string.moodTracking_moodType_update_selectAnotherDate_text))
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

@Composable
private fun SelectTimeSection(
    dateTimeFormatter: DateTimeFormatter,
    timeInputController: InputController<LocalTime>,
) {
    val timeInputControllerState by timeInputController.state.collectAsState()
    val formattedDate = dateTimeFormatter.formatTime(timeInputControllerState.input)
    var dialogOpen by remember {
        mutableStateOf(false)
    }

    TextIconButton(
        textIconButtonConfig = TextIconButtonConfig.Outlined(
            iconResId = R.drawable.ic_time,
            labelResId = R.string.dateTime_selectedTime_formatText,
            formatArgs = listOf(formattedDate),
            onClick = {
                dialogOpen = true
            },
        ),
    )

    TimePickerDialog(
        dialogOpen = dialogOpen,
        onUpdateDialogOpen = { dialogOpen = it },
        timeInputController = timeInputController,
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun MoodTrackingUpdatePreview() {
    HealtherTheme {
        MoodTrackingUpdate(
            onGoBack = {},
            onDeleteShowDialog = {},
            onManageMoodActivities = {},
            onManageMoodTypes = {},
            dateTimeProvider = DateTimeProvider(dispatchers = DefaultBackgroundBackgroundCoroutineDispatchers),
            dateTimeFormatter = DateTimeFormatter(LocalContext.current),
            dateInputController = MockControllersProvider.inputController(MockDateProvider.localDate()),
            timeInputController = MockControllersProvider.inputController(MockDateProvider.localTime()),
            updateController = MockControllersProvider.requestController(),
            noteInputController = MockControllersProvider.inputController(""),
            moodTypeSelectionController = MockControllersProvider.singleSelectionController(
                dataList = MoodTrackingMockDataProvider.moodTypesList(
                    context = LocalContext.current,
                ),
            ),
            activitiesMultiSelectionController = MockControllersProvider.multiSelectionController(
                dataList = MoodTrackingMockDataProvider.moodActivitiesList(
                    context = LocalContext.current,
                ),
            ),
        )
    }
}