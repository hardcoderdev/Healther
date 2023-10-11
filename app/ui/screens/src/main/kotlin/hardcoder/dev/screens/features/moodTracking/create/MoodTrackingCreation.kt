package hardcoder.dev.screens.features.moodTracking.create

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.MultiSelectionController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.coroutines.DefaultBackgroundBackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.entities.features.moodTracking.MoodActivity
import hardcoder.dev.entities.features.moodTracking.MoodType
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.icons.resourceId
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.mock.dataProviders.features.MoodTrackingMockDataProvider
import hardcoder.dev.screens.features.moodTracking.moodType.MoodItem
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
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
import hardcoder.dev.uikit.components.text.textField.TextInputAdapter
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.sections.dateTime.DateTimeSection
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Composable
fun MoodTrackingCreation(
    dateTimeProvider: DateTimeProvider,
    dateTimeFormatter: DateTimeFormatter,
    moodTypeSelectionController: SingleSelectionController<MoodType>,
    dateInputController: InputController<LocalDate>,
    timeInputController: InputController<LocalTime>,
    noteInputController: InputController<String>,
    activitiesMultiSelectionController: MultiSelectionController<MoodActivity>,
    creationController: RequestController,
    onManageMoodTypes: () -> Unit,
    onManageMoodActivities: () -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            MoodTrackingCreationContent(
                dateTimeProvider = dateTimeProvider,
                dateTimeFormatter = dateTimeFormatter,
                noteInputController = noteInputController,
                moodTypeSelectionController = moodTypeSelectionController,
                activitiesMultiSelectionController = activitiesMultiSelectionController,
                dateInputController = dateInputController,
                timeInputController = timeInputController,
                creationController = creationController,
                onManageMoodTypes = onManageMoodTypes,
                onManageActivities = onManageMoodActivities,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.tracking_creation_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun MoodTrackingCreationContent(
    dateTimeProvider: DateTimeProvider,
    dateTimeFormatter: DateTimeFormatter,
    moodTypeSelectionController: SingleSelectionController<MoodType>,
    dateInputController: InputController<LocalDate>,
    timeInputController: InputController<LocalTime>,
    noteInputController: InputController<String>,
    activitiesMultiSelectionController: MultiSelectionController<MoodActivity>,
    creationController: RequestController,
    onManageActivities: () -> Unit,
    onManageMoodTypes: () -> Unit,
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
                activitiesMultiSelectionController = activitiesMultiSelectionController,
                onManageActivities = onManageActivities,
            )
            Spacer(modifier = Modifier.height(16.dp))
            DateTimeSection(
                dateTimeProvider = dateTimeProvider,
                dateTimeFormatter = dateTimeFormatter,
                dateInputController = dateInputController,
                timeInputController = timeInputController,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            requestButtonConfig = RequestButtonConfig.Filled(
                controller = creationController,
                iconResId = R.drawable.ic_save,
                labelResId = R.string.tracking_createEntry_buttonText,
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
                        contentDescription = stringResource(id = R.string.moodTracking_moodType_creation_manageMoodTypes_buttonText),
                        alignment = Alignment.Center,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Label(text = stringResource(id = R.string.moodTracking_moodType_creation_manageMoodTypes_buttonText))
                }
            },
        ),
    )
}

@Composable
private fun SelectActivitiesSection(
    activitiesMultiSelectionController: MultiSelectionController<MoodActivity>,
    onManageActivities: () -> Unit,
) {
    Title(text = stringResource(id = R.string.moodTracking_creation_youMaySelectActivities_text))
    Spacer(modifier = Modifier.height(16.dp))
    MultiSelectionChipFlowRow(
        controller = activitiesMultiSelectionController,
        maxItemsInEachRow = 6,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        itemModifier = Modifier.padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.Center,
        actionButton = { ManagementActivitiesButton(onManageActivities = onManageActivities) },
        emptyContent = { ManagementActivitiesButton(onManageActivities = onManageActivities) },
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
            inputAdapter = TextInputAdapter,
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

@HealtherScreenPhonePreviews
@Composable
private fun MoodTrackingCreationPreview() {
    HealtherTheme {
        MoodTrackingCreation(
            onManageMoodTypes = {},
            onManageMoodActivities = {},
            onGoBack = {},
            dateTimeProvider = DateTimeProvider(dispatchers = DefaultBackgroundBackgroundCoroutineDispatchers),
            dateTimeFormatter = DateTimeFormatter(LocalContext.current),
            dateInputController = MockControllersProvider.inputController(MockDateProvider.localDate()),
            timeInputController = MockControllersProvider.inputController(MockDateProvider.localTime()),
            creationController = MockControllersProvider.requestController(),
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