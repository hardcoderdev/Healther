package hardcoder.dev.androidApp.ui.features.moodTracking.update

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.LaunchedEffect
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
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.moodTracking.activity.Activity
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingTrackUpdateViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.ButtonStyles
import hardcoder.dev.uikit.buttons.ButtonWithIcon
import hardcoder.dev.uikit.card.ActionCard
import hardcoder.dev.uikit.chip.ActionChip
import hardcoder.dev.uikit.chip.SelectionChip
import hardcoder.dev.uikit.dialogs.DatePicker
import hardcoder.dev.uikit.lists.ScrollableTabRow
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.FilledTextField
import hardcoder.dev.uikit.text.Label
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
    val viewModel = viewModel {
        presentationModule.getMoodTrackingUpdateViewModel(moodTrackId)
    }
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.value.updateState) {
        if (state.value.updateState is MoodTrackingTrackUpdateViewModel.UpdateState.Executed) {
            onGoBack()
        }
    }

    LaunchedEffect(key1 = state.value.deleteState) {
        if (state.value.deleteState is MoodTrackingTrackUpdateViewModel.DeleteState.Executed) {
            onGoBack()
        }
    }

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    DeleteTrackDialog(
        dialogOpen = dialogOpen,
        onUpdateDialogOpen = { dialogOpen = it },
        onCancel = { dialogOpen = false },
        onApprove = {
            viewModel.deleteTrack()
            dialogOpen = false
        }
    )

    ScaffoldWrapper(
        content = {
            UpdateMoodTrackContent(
                state = state.value,
                onUpdateMoodType = viewModel::updateSelectedMoodType,
                onUpdateNote = viewModel::updateNote,
                onUpdateSelectedDate = viewModel::updateSelectedDate,
                onManageMoodTypes = onManageMoodTypes,
                onToggleActivity = viewModel::toggleActivity,
                onManageActivities = onManageActivities,
                onUpdateTrack = viewModel::updateTrack
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
    state: MoodTrackingTrackUpdateViewModel.State,
    onUpdateMoodType: (MoodType) -> Unit,
    onUpdateSelectedDate: (LocalDateTime) -> Unit,
    onUpdateNote: (String) -> Unit,
    onManageMoodTypes: () -> Unit,
    onManageActivities: () -> Unit,
    onToggleActivity: (Activity) -> Unit,
    onUpdateTrack: () -> Unit
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
                state = state,
                onUpdateMoodType = onUpdateMoodType,
                onManageMoodTypes = onManageMoodTypes
            )
            Spacer(modifier = Modifier.height(16.dp))
            EnterNoteSection(state = state, onUpdateNote = onUpdateNote)
            Spacer(modifier = Modifier.height(16.dp))
            SelectActivitiesSection(
                state = state,
                onToggleActivity = onToggleActivity,
                onManageActivities = onManageActivities
            )
            Spacer(modifier = Modifier.height(16.dp))
            SelectDateSection(
                state = state,
                onShowDatePicker = { datePickerDialogVisibility = !datePickerDialogVisibility }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        ButtonWithIcon(
            iconResId = R.drawable.ic_save,
            labelResId = R.string.moodTracking_updateMoodTrack_buttonText,
            onClick = onUpdateTrack
        )
        DatePicker(
            onUpdateSelectedDate = onUpdateSelectedDate,
            isShowing = datePickerDialogVisibility,
            onClose = {
                datePickerDialogVisibility = !datePickerDialogVisibility
            }
        )
    }
}

@Composable
private fun SelectMoodSection(
    state: MoodTrackingTrackUpdateViewModel.State,
    onUpdateMoodType: (MoodType) -> Unit,
    onManageMoodTypes: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Title(text = stringResource(id = R.string.moodTracking_createMoodTrack_howYouFeelYourself_text))
        Spacer(modifier = Modifier.height(16.dp))
        ScrollableTabRow(selectedTabIndex = state.moodTypeList.indexOf(state.selectedMoodType)) {
            MoodTypeManagementButton(onManageMoodTypes = onManageMoodTypes)
            state.moodTypeList.forEach { moodType ->
                MoodItem(
                    modifier = Modifier.padding(
                        start = 4.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 16.dp
                    ),
                    moodType = moodType,
                    selectedMoodType = state.selectedMoodType,
                    onSelect = { onUpdateMoodType(moodType) }
                )
            }
        }
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectActivitiesSection(
    state: MoodTrackingTrackUpdateViewModel.State,
    onToggleActivity: (Activity) -> Unit,
    onManageActivities: () -> Unit
) {
    Title(text = stringResource(id = R.string.moodTracking_createMoodTrack_youMaySelectActivities_text))
    Spacer(modifier = Modifier.height(16.dp))
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        maxItemsInEachRow = 6
    ) {
        ManagementActivitiesButton(onManageActivities = onManageActivities)
        state.activityList.forEach { activity ->
            SelectionChip(
                modifier = Modifier.padding(top = 8.dp),
                text = activity.name,
                iconResId = activity.icon.resourceId,
                shape = RoundedCornerShape(32.dp),
                isSelected = state.selectedHobbies.contains(activity),
                onClick = { onToggleActivity(activity) }
            )
        }
    }
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
private fun EnterNoteSection(
    state: MoodTrackingTrackUpdateViewModel.State,
    onUpdateNote: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Title(text = stringResource(id = R.string.moodTracking_createMoodTrack_youCanAddNote_text))
        Spacer(modifier = Modifier.height(16.dp))
        FilledTextField(
            value = state.note ?: "",
            onValueChange = onUpdateNote,
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
    state: MoodTrackingTrackUpdateViewModel.State,
    onShowDatePicker: () -> Unit
) {
    val uiModule = LocalUIModule.current
    val dateTimeFormatter = uiModule.dateTimeFormatter
    val selectedDate = state.selectedDate.toInstant(TimeZone.currentSystemDefault())
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