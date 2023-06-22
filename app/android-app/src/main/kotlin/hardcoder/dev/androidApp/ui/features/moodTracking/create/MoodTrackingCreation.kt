package hardcoder.dev.androidApp.ui.features.moodTracking.create

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
import hardcoder.dev.androidApp.ui.features.moodTracking.MoodItem
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.MultiSelectionController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivity
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingCreationViewModel
import hardcoder.dev.uikit.LaunchedEffectWhenExecuted
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.lists.flowRow.MultiSelectionChipFlowRow
import hardcoder.dev.uikit.lists.SingleCardSelectionScrollableTabRow
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.ButtonStyles
import hardcoder.dev.uikit.buttons.ButtonWithIcon
import hardcoder.dev.uikit.buttons.RequestButtonWithIcon
import hardcoder.dev.uikit.card.ActionCard
import hardcoder.dev.uikit.chip.ActionChip
import hardcoder.dev.uikit.chip.content.ChipIconDefaultContent
import hardcoder.dev.uikit.dialogs.DatePicker
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.ErrorText
import hardcoder.dev.uikit.text.Label
import hardcoder.dev.uikit.text.TextField
import hardcoder.dev.uikit.text.Title
import hardcoderdev.healther.app.android.app.R
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun MoodTrackingCreation(
    onGoBack: () -> Unit,
    onManageMoodActivities: () -> Unit,
    onManageMoodTypes: () -> Unit
) {
    val viewModel = koinViewModel<MoodTrackingCreationViewModel>()

    LaunchedEffectWhenExecuted(controller = viewModel.creationController, action = onGoBack)

    ScaffoldWrapper(
        content = {
            MoodTrackingCreationContent(
                noteController = viewModel.noteController,
                moodTypeSelectionController = viewModel.moodTypeSelectionController,
                activitiesMultiSelectionController = viewModel.activitiesMultiSelectionController,
                dateController = viewModel.dateController,
                creationController = viewModel.creationController,
                onManageMoodTypes = onManageMoodTypes,
                onManageActivities = onManageMoodActivities,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_createMoodTrack_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun MoodTrackingCreationContent(
    moodTypeSelectionController: SingleSelectionController<MoodType>,
    dateController: InputController<LocalDateTime>,
    noteController: InputController<String>,
    activitiesMultiSelectionController: MultiSelectionController<MoodActivity>,
    creationController: SingleRequestController,
    onManageActivities: () -> Unit,
    onManageMoodTypes: () -> Unit,
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
            EnterNoteSection(noteController = noteController)
            Spacer(modifier = Modifier.height(16.dp))
            SelectActivitiesSection(
                activitiesMultiSelectionController = activitiesMultiSelectionController,
                onManageActivities = onManageActivities
            )
            Spacer(modifier = Modifier.height(16.dp))
            SelectDateSection(
                dateController = dateController,
                onShowDatePicker = { datePickerDialogVisibility = !datePickerDialogVisibility }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            controller = creationController,
            iconResId = R.drawable.ic_save,
            labelResId = R.string.moodTracking_createMoodTrack_buttonText
        )
        DatePicker(
            onUpdateSelectedDate = dateController::changeInput,
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
        SingleCardSelectionScrollableTabRow(
            controller = moodTypeSelectionController,
            actionButton = { MoodTypeManagementButton(onManageMoodTypes = onManageMoodTypes) },
            emptyContent = {
                MoodTypeManagementButton(onManageMoodTypes = onManageMoodTypes)
                Spacer(modifier = Modifier.height(8.dp))
                ErrorText(text = stringResource(id = R.string.moodTracking_createMoodTrack_moodTypeNotSelected_text))
            },
            itemContent = { moodType, _ ->
                MoodItem(
                    moodType = moodType,
                    modifier = Modifier.padding(
                        start = 4.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 16.dp
                    )
                )
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
    activitiesMultiSelectionController: MultiSelectionController<MoodActivity>,
    onManageActivities: () -> Unit
) {
    Title(text = stringResource(id = R.string.moodTracking_createMoodTrack_youMaySelectActivities_text))
    Spacer(modifier = Modifier.height(16.dp))
    MultiSelectionChipFlowRow(
        controller = activitiesMultiSelectionController,
        maxItemsInEachRow = 6,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        itemModifier = Modifier.padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        actionButton = { ManagementActivitiesButton(onManageActivities = onManageActivities) },
        emptyContent = { ManagementActivitiesButton(onManageActivities = onManageActivities) },
        itemContent = { activity, _ ->
            ChipIconDefaultContent(
                iconResId = activity.icon.resourceId,
                name = activity.name
            )
        }
    )
}

@Composable
private fun ManagementActivitiesButton(onManageActivities: () -> Unit) {
    ActionChip(
        modifier = Modifier.padding(top = 8.dp),
        text = stringResource(id = R.string.moodTracking_createMoodTrack_manageActivities_buttonText),
        iconResId = R.drawable.ic_create,
        shape = RoundedCornerShape(32.dp),
        onClick = onManageActivities
    )
}

@Composable
private fun EnterNoteSection(noteController: InputController<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Title(text = stringResource(id = R.string.moodTracking_createMoodTrack_youCanAddNote_text))
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            controller = noteController,
            label = R.string.moodTracking_createMoodTrack_enterNote_textField,
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
    dateController: InputController<LocalDateTime>,
    onShowDatePicker: () -> Unit
) {
    val state by dateController.state.collectAsState()

    val dateTimeFormatter = koinInject<DateTimeFormatter>()
    val selectedDate = state.input.toInstant(TimeZone.currentSystemDefault())
    val formattedDate = dateTimeFormatter.formatDateTime(selectedDate)

    Title(text = stringResource(id = R.string.moodTracking_createMoodTrack_selectAnotherDate_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(
        text = stringResource(
            id = R.string.moodTracking_createMoodTrack_selectedDate_formatText,
            formatArgs = arrayOf(formattedDate)
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    ButtonWithIcon(
        iconResId = R.drawable.ic_date_range,
        labelResId = R.string.moodTracking_createMoodTrack_selectDateRange_buttonText,
        style = ButtonStyles.OUTLINED,
        onClick = onShowDatePicker
    )
}