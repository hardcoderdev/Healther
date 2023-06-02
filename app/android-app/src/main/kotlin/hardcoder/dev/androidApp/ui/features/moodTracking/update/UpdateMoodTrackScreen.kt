package hardcoder.dev.androidApp.ui.features.moodTracking.update

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.di.LocalUIModule
import hardcoder.dev.androidApp.ui.features.DeleteTrackDialog
import hardcoder.dev.androidApp.ui.features.moodTracking.MoodItem
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.MultiSelectionController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.moodTracking.activity.Activity
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.LaunchedEffectWhenExecuted
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.ButtonStyles
import hardcoder.dev.uikit.buttons.ButtonWithIcon
import hardcoder.dev.uikit.buttons.RequestButtonWithIcon
import hardcoder.dev.uikit.card.ActionCard
import hardcoder.dev.uikit.chip.ActionChip
import hardcoder.dev.uikit.dialogs.DatePicker
import hardcoder.dev.uikit.icons.Icon
import hardcoder.dev.uikit.lists.SingleSelectionScrollableTabRow
import hardcoder.dev.uikit.lists.flowRow.MultiSelectionChipFlowRow
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.ErrorText
import hardcoder.dev.uikit.text.Label
import hardcoder.dev.uikit.text.TextField
import hardcoder.dev.uikit.text.Title
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Composable
fun UpdateMoodTrackScreen(
    moodTrackId: Int,
    onGoBack: () -> Unit,
    onManageActivities: () -> Unit,
    onManageMoodTypes: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel { presentationModule.getMoodTrackingUpdateViewModel(moodTrackId) }

    LaunchedEffectWhenExecuted(controller = viewModel.updateController, action = onGoBack)
    LaunchedEffectWhenExecuted(controller = viewModel.deleteController, action = onGoBack)

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    DeleteTrackDialog(
        dialogOpen = dialogOpen,
        onUpdateDialogOpen = { dialogOpen = it },
        onCancel = { dialogOpen = false },
        onApprove = {
            viewModel.deleteController::request
            dialogOpen = false
        }
    )

    ScaffoldWrapper(
        content = {
            UpdateMoodTrackContent(
                dateInputController = viewModel.dateController,
                noteInputController = viewModel.noteInputController,
                moodTypeSelectionController = viewModel.moodTypeSelectionController,
                updateController = viewModel.updateController,
                multiSelectionController = viewModel.activitiesMultiSelectionController,
                onManageMoodTypes = onManageMoodTypes,
                onManageActivities = onManageActivities,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_createMoodTrack_title_topBar,
                onGoBack = onGoBack
            )
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_delete,
                    onActionClick = { dialogOpen = true }
                )
            )
        )
    )
}

@Composable
private fun UpdateMoodTrackContent(
    moodTypeSelectionController: SingleSelectionController<MoodType>,
    multiSelectionController: MultiSelectionController<Activity>,
    dateInputController: InputController<LocalDateTime>,
    noteInputController: InputController<String>,
    updateController: SingleRequestController,
    onManageMoodTypes: () -> Unit,
    onManageActivities: () -> Unit
) {
    var datePickerDialogVisibility by remember {
        mutableStateOf(false)
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            Modifier
                .weight(2f)
                .verticalScroll(rememberScrollState())
        ) {
            SelectMoodSection(
                moodTypeSelectionController = moodTypeSelectionController,
                onManageMoodTypes = onManageMoodTypes
            )
            Spacer(modifier = Modifier.height(16.dp))
            EnterNoteSection(noteInputController = noteInputController)
            Spacer(modifier = Modifier.height(16.dp))
            SelectActivitiesSection(
                multiSelectionController = multiSelectionController,
                onManageActivities = onManageActivities
            )
            Spacer(modifier = Modifier.height(16.dp))
            SelectDateSection(
                dateInputController = dateInputController,
                onShowDatePicker = {
                    datePickerDialogVisibility = !datePickerDialogVisibility
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            controller = updateController,
            iconResId = R.drawable.ic_save,
            labelResId = R.string.moodTracking_updateMoodTrack_buttonText
        )
        DatePicker(
            onUpdateSelectedDate = dateInputController::changeInput,
            isShowing = datePickerDialogVisibility,
            onClose = {
                datePickerDialogVisibility = !datePickerDialogVisibility
            }
        )
    }
}

@Composable
private fun SelectMoodSection(
    moodTypeSelectionController: SingleSelectionController<MoodType>,
    onManageMoodTypes: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Title(text = stringResource(id = R.string.moodTracking_createMoodTrack_howYouFeelYourself_text))
        Spacer(modifier = Modifier.height(16.dp))
        SingleSelectionScrollableTabRow(
            controller = moodTypeSelectionController,
            actionButton = { MoodTypeManagementButton(onManageMoodTypes = onManageMoodTypes) },
            emptyContent = {
                MoodTypeManagementButton(onManageMoodTypes = onManageMoodTypes)
                Spacer(modifier = Modifier.height(8.dp))
                ErrorText(text = stringResource(id = R.string.moodTracking_updateMoodTrack_moodTypeNotSelected_text))
            },
            itemContent = { moodTypeList, selectedMoodType ->
                moodTypeList.forEach { moodType ->
                    MoodItem(
                        modifier = Modifier.padding(
                            start = 4.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 16.dp
                        ),
                        moodType = moodType,
                        selectedMoodType = selectedMoodType,
                        onSelect = { moodTypeSelectionController.select(moodType) }
                    )
                }
            }
        )
    }
}

@Composable
private fun MoodTypeManagementButton(onManageMoodTypes: () -> Unit) {
    ActionCard(
        onClick = onManageMoodTypes,
        modifier = Modifier.padding(
            start = 4.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 16.dp
        )
    ) {
        Column(
            modifier = Modifier
                .width(130.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.height(60.dp),
                painter = painterResource(id = R.drawable.ic_create),
                contentDescription = stringResource(id = R.string.moodTracking_createMoodTrack_manageMoodTypes_buttonText),
                alignment = Alignment.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Label(text = stringResource(id = R.string.moodTracking_createMoodTrack_manageMoodTypes_buttonText))
        }
    }
}

@Composable
private fun SelectActivitiesSection(
    multiSelectionController: MultiSelectionController<Activity>,
    onManageActivities: () -> Unit
) {
    Title(text = stringResource(id = R.string.moodTracking_createMoodTrack_youMaySelectActivities_text))
    Spacer(modifier = Modifier.height(16.dp))
    MultiSelectionChipFlowRow(
        controller = multiSelectionController,
        maxItemsInEachRow = 6,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        itemModifier = Modifier.padding(top = 8.dp),
        emptyContent = { ManagementActivitiesButton(onManageActivities = onManageActivities) },
        actionButton = { ManagementActivitiesButton(onManageActivities = onManageActivities) },
        itemContent = { activity, _ ->
            Icon(
                iconResId = activity.icon.resourceId,
                contentDescription = activity.name
            )
            Spacer(modifier = Modifier.width(8.dp))
            Label(text = activity.name)
        }
    )
}

@Composable
private fun ManagementActivitiesButton(onManageActivities: () -> Unit) {
    ActionChip(
        modifier = Modifier.padding(top = 8.dp),
        text = stringResource(id = R.string.moodTracking_updateMoodTrack_manageActivities_buttonText),
        iconResId = R.drawable.ic_create,
        shape = RoundedCornerShape(32.dp),
        onClick = onManageActivities
    )
}


@Composable
private fun EnterNoteSection(noteInputController: InputController<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Title(text = stringResource(id = R.string.moodTracking_createMoodTrack_youCanAddNote_text))
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            controller = noteInputController,
            label = R.string.moodTracking_updateMoodTrack_enterNote_textField,
            multiline = true,
            maxLines = 5,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )
    }
}

@Composable
private fun SelectDateSection(
    dateInputController: InputController<LocalDateTime>,
    onShowDatePicker: () -> Unit
) {
    val uiModule = LocalUIModule.current
    val dateTimeFormatter = uiModule.dateTimeFormatter
    val dateInputControllerState = dateInputController.state.collectAsState()

    val selectedDate =
        dateInputControllerState.value.input.toInstant(TimeZone.currentSystemDefault())
    val formattedDate = dateTimeFormatter.formatDateTime(selectedDate)

    Title(text = stringResource(id = R.string.moodTracking_updateMoodTrack_selectAnotherDate_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(
        text = stringResource(
            id = R.string.moodTracking_updateMoodTrack_selectedDate_formatText,
            formatArgs = arrayOf(formattedDate)
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    ButtonWithIcon(
        iconResId = R.drawable.ic_date_range,
        labelResId = R.string.moodTracking_updateMoodTrack_selectDateRange_buttonText,
        style = ButtonStyles.OUTLINED,
        onClick = onShowDatePicker
    )
}