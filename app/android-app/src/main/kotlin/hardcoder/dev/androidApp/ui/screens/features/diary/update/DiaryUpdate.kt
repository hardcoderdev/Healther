package hardcoder.dev.androidApp.ui.screens.features.diary.update

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.screens.features.fasting.FastingItem
import hardcoder.dev.androidApp.ui.screens.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.androidApp.ui.screens.features.moodTracking.MoodTrackItem
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.MultiSelectionController
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.formatters.MillisDistanceFormatter
import hardcoder.dev.icons.resourceId
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.features.diary.diaryTrack.IncorrectDiaryTrackContent
import hardcoder.dev.logic.features.diary.diaryTrack.ValidatedDiaryTrackContent
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.DiaryMockDataProvider
import hardcoder.dev.presentation.features.diary.DiaryUpdateViewModel
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.chip.Chip
import hardcoder.dev.uikit.components.chip.ChipConfig
import hardcoder.dev.uikit.components.chip.content.ChipIconDefaultContent
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.list.flowRow.MultiSelectionChipFlowRow
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.text.textField.TextFieldValidationAdapter
import hardcoder.dev.uikit.components.text.textField.TextInputAdapter
import hardcoder.dev.uikit.components.text.textField.ValidatedTextField
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R

@Composable
fun DiaryUpdate(
    millisDistanceFormatter: MillisDistanceFormatter,
    fastingPlanResourcesProvider: FastingPlanResourcesProvider,
    dateTimeFormatter: DateTimeFormatter,
    diaryAttachmentsLoadingController: LoadingController<DiaryUpdateViewModel.ReadOnlyDiaryAttachments>,
    contentInputController: ValidatedInputController<String, ValidatedDiaryTrackContent>,
    tagMultiSelectionController: MultiSelectionController<DiaryTag>,
    updateController: RequestController,
    deleteController: RequestController,
    onManageTags: () -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            DiaryUpdateContent(
                millisDistanceFormatter = millisDistanceFormatter,
                fastingPlanResourcesProvider = fastingPlanResourcesProvider,
                dateTimeFormatter = dateTimeFormatter,
                contentInputController = contentInputController,
                tagMultiSelectionController = tagMultiSelectionController,
                updateController = updateController,
                diaryAttachmentsLoadingController = diaryAttachmentsLoadingController,
                onManageTags = onManageTags,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.diary_update_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_delete,
                    onActionClick = deleteController::request,
                ),
            ),
        ),
    )
}

@Composable
private fun DiaryUpdateContent(
    millisDistanceFormatter: MillisDistanceFormatter,
    fastingPlanResourcesProvider: FastingPlanResourcesProvider,
    dateTimeFormatter: DateTimeFormatter,
    diaryAttachmentsLoadingController: LoadingController<DiaryUpdateViewModel.ReadOnlyDiaryAttachments>,
    contentInputController: ValidatedInputController<String, ValidatedDiaryTrackContent>,
    tagMultiSelectionController: MultiSelectionController<DiaryTag>,
    updateController: RequestController,
    onManageTags: () -> Unit,
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
            EnterBasicInfoSection(contentInputController = contentInputController)
            LoadingContainer(
                controller = diaryAttachmentsLoadingController,
                loadedContent = { readOnlyAttachments ->
                    if (!readOnlyAttachments.isEmpty) {
                        Spacer(modifier = Modifier.height(32.dp))
                        AttachedEntitySection(
                            millisDistanceFormatter = millisDistanceFormatter,
                            fastingPlanResourcesProvider = fastingPlanResourcesProvider,
                            dateTimeFormatter = dateTimeFormatter,
                            readOnlyDiaryAttachments = readOnlyAttachments,
                        )
                    }
                },
            )
            Spacer(modifier = Modifier.height(32.dp))
            SelectTagsSection(
                tagMultiSelectionController = tagMultiSelectionController,
                onManageTags = onManageTags,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            requestButtonConfig = RequestButtonConfig.Filled(
                controller = updateController,
                iconResId = R.drawable.ic_save,
                labelResId = R.string.diary_creation_buttonText,
            ),
        )
    }
}

@Composable
private fun EnterBasicInfoSection(
    contentInputController: ValidatedInputController<String, ValidatedDiaryTrackContent>,
) {
    val context = LocalContext.current

    Title(text = stringResource(id = R.string.diary_creation_enterInfo_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        controller = contentInputController,
        modifier = Modifier.fillMaxWidth(),
        label = R.string.diary_creation_enterNote_textField,
        multiline = true,
        minLines = 5,
        maxLines = 5,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default,
        ),
        inputAdapter = TextInputAdapter,
        validationAdapter = TextFieldValidationAdapter {
            if (it !is IncorrectDiaryTrackContent) {
                null
            } else {
                when (it.reason) {
                    is IncorrectDiaryTrackContent.Reason.Empty -> {
                        context.getString(R.string.errors_fieldCantBeEmptyError)
                    }
                }
            }
        },
    )
}

@Composable
private fun AttachedEntitySection(
    millisDistanceFormatter: MillisDistanceFormatter,
    dateTimeFormatter: DateTimeFormatter,
    fastingPlanResourcesProvider: FastingPlanResourcesProvider,
    readOnlyDiaryAttachments: DiaryUpdateViewModel.ReadOnlyDiaryAttachments,
) {
    Title(text = stringResource(id = R.string.diary_update_attachedEntity_text))
    Spacer(modifier = Modifier.height(16.dp))
    readOnlyDiaryAttachments.fastingTracks.forEach { fastingTrack ->
        FastingItem(
            dateTimeFormatter = dateTimeFormatter,
            millisDistanceFormatter = millisDistanceFormatter,
            fastingPlanResourcesProvider = fastingPlanResourcesProvider,
            fastingTrack = fastingTrack,
        )
    }
    readOnlyDiaryAttachments.moodTracks.forEach { moodTrack ->
        MoodTrackItem(
            dateTimeFormatter = dateTimeFormatter,
            activitiesList = emptyList(),
            moodTrack = moodTrack,
            onUpdate = {},
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun SelectTagsSection(
    tagMultiSelectionController: MultiSelectionController<DiaryTag>,
    onManageTags: () -> Unit,
) {
    Title(text = stringResource(id = R.string.diary_creation_youMaySelectTags_text))
    Spacer(modifier = Modifier.height(16.dp))
    MultiSelectionChipFlowRow(
        controller = tagMultiSelectionController,
        maxItemsInEachRow = 6,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        itemModifier = Modifier.padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.Center,
        actionButton = { ManagementTagsButton(onManageTags = onManageTags) },
        emptyContent = { ManagementTagsButton(onManageTags = onManageTags) },
        itemContent = { tag, _ ->
            ChipIconDefaultContent(
                iconResId = tag.icon.resourceId,
                name = tag.name,
            )
        },
    )
}

@Composable
private fun ManagementTagsButton(onManageTags: () -> Unit) {
    Chip(
        chipConfig = ChipConfig.Action(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(id = R.string.diary_tags_title_topBar),
            iconResId = R.drawable.ic_create,
            shape = RoundedCornerShape(32.dp),
            onClick = onManageTags,
        ),
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun DiaryUpdatePreview() {
    HealtherTheme {
        DiaryUpdate(
            onGoBack = {},
            onManageTags = {},
            dateTimeFormatter = DateTimeFormatter(context = LocalContext.current),
            fastingPlanResourcesProvider = FastingPlanResourcesProvider(),
            millisDistanceFormatter = MillisDistanceFormatter(
                context = LocalContext.current,
                defaultAccuracy = MillisDistanceFormatter.Accuracy.DAYS,
            ),
            updateController = MockControllersProvider.requestController(),
            deleteController = MockControllersProvider.requestController(),
            contentInputController = MockControllersProvider.validatedInputController(""),
            diaryAttachmentsLoadingController = MockControllersProvider.loadingController(
                data = DiaryMockDataProvider.diaryAttachmentsMoodTrack(
                    context = LocalContext.current,
                    isWithTags = true,
                ),
            ),
            tagMultiSelectionController = MockControllersProvider.multiSelectionController(
                dataList = DiaryMockDataProvider.diaryTagsList(
                    context = LocalContext.current,
                ),
            ),
        )
    }
}