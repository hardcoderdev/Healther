package hardcoder.dev.androidApp.ui.features.diary.update

import android.content.Context
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.features.fasting.FastingItem
import hardcoder.dev.androidApp.ui.features.moodTracking.MoodTrackItem
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.MultiSelectionController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.features.diary.diaryTrack.IncorrectDiaryTrackContent
import hardcoder.dev.logic.features.diary.diaryTrack.ValidatedDiaryTrackContent
import hardcoder.dev.presentation.features.diary.DiaryUpdateViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.LaunchedEffectWhenExecuted
import hardcoder.dev.uikit.LoadingContainer
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.RequestButtonWithIcon
import hardcoder.dev.uikit.chip.ActionChip
import hardcoder.dev.uikit.chip.content.ChipIconDefaultContent
import hardcoder.dev.uikit.lists.flowRow.MultiSelectionChipFlowRow
import hardcoder.dev.uikit.text.Title
import hardcoder.dev.uikit.text.ValidatedTextField
import hardcoder.dev.uikit.text.rememberValidationAdapter
import hardcoderdev.healther.app.android.app.R
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DiaryUpdateTrackScreen(
    diaryTrackId: Int,
    onGoBack: () -> Unit,
    onManageTags: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = koinViewModel<DiaryUpdateViewModel> {
        parametersOf(diaryTrackId)
    }

    LaunchedEffectWhenExecuted(controller = viewModel.updateController, action = onGoBack)
    LaunchedEffectWhenExecuted(controller = viewModel.deleteController, action = onGoBack)

    ScaffoldWrapper(
        content = {
            DiaryUpdateTrackContent(
                context = context,
                contentInputController = viewModel.contentInputController,
                tagMultiSelectionController = viewModel.tagMultiSelectionController,
                updateController = viewModel.updateController,
                diaryAttachmentsLoadingController = viewModel.diaryAttachmentsLoadingController,
                onManageTags = onManageTags
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.diary_updateTrack_title_topBar,
                onGoBack = onGoBack
            )
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_delete,
                    onActionClick = viewModel.deleteController::request
                )
            )
        )
    )
}

@Composable
private fun DiaryUpdateTrackContent(
    context: Context,
    diaryAttachmentsLoadingController: LoadingController<DiaryUpdateViewModel.ReadOnlyDiaryAttachments>,
    contentInputController: ValidatedInputController<String, ValidatedDiaryTrackContent>,
    tagMultiSelectionController: MultiSelectionController<DiaryTag>,
    updateController: SingleRequestController,
    onManageTags: () -> Unit
) {
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
            EnterBasicInfoSection(
                context = context,
                contentInputController = contentInputController
            )
            LoadingContainer(
                controller = diaryAttachmentsLoadingController,
                loadedContent = { readOnlyAttachments ->
                    if (!readOnlyAttachments.isEmpty) {
                        Spacer(modifier = Modifier.height(32.dp))
                        AttachedEntitySection(readOnlyDiaryAttachments = readOnlyAttachments)
                    }
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            SelectTagsSection(
                tagMultiSelectionController = tagMultiSelectionController,
                onManageTags = onManageTags
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            controller = updateController,
            iconResId = R.drawable.ic_save,
            labelResId = R.string.diary_createTrack_buttonText
        )
    }
}

@Composable
private fun EnterBasicInfoSection(
    context: Context,
    contentInputController: ValidatedInputController<String, ValidatedDiaryTrackContent>
) {
    Title(text = stringResource(id = R.string.diary_updateTrack_enterInfo_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        controller = contentInputController,
        modifier = Modifier.fillMaxWidth(),
        label = R.string.diary_updateTrack_enterNote_textField,
        multiline = true,
        minLines = 5,
        maxLines = 5,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default
        ),
        validationAdapter = rememberValidationAdapter {
            if (it !is IncorrectDiaryTrackContent) null
            else when (it.reason) {
                is IncorrectDiaryTrackContent.Reason.Empty -> {
                    context.getString(R.string.diary_updateTrack_descriptionEmpty_text)
                }
            }
        }
    )
}

@Composable
private fun AttachedEntitySection(readOnlyDiaryAttachments: DiaryUpdateViewModel.ReadOnlyDiaryAttachments) {
    Title(text = stringResource(id = R.string.diary_updateTrack_attachedEntity_text))
    Spacer(modifier = Modifier.height(16.dp))
    readOnlyDiaryAttachments.fastingTracks.forEach { fastingTrack ->
        FastingItem(fastingTrack = fastingTrack)
    }
    readOnlyDiaryAttachments.moodTracks.forEach { moodTrack ->
        MoodTrackItem(
            activitiesList = emptyList(),
            moodTrack = moodTrack,
            onUpdate = {}
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun SelectTagsSection(
    tagMultiSelectionController: MultiSelectionController<DiaryTag>,
    onManageTags: () -> Unit
) {
    Title(text = stringResource(id = R.string.diary_updateTrack_youMaySelectTags_text))
    Spacer(modifier = Modifier.height(16.dp))
    MultiSelectionChipFlowRow(
        controller = tagMultiSelectionController,
        maxItemsInEachRow = 6,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        itemModifier = Modifier.padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        actionButton = { ManagementTagsButton(onManageTags = onManageTags) },
        emptyContent = { ManagementTagsButton(onManageTags = onManageTags) },
        itemContent = { tag, _ ->
            ChipIconDefaultContent(
                iconResId = tag.icon.resourceId,
                name = tag.name
            )
        }
    )
}

@Composable
private fun ManagementTagsButton(onManageTags: () -> Unit) {
    ActionChip(
        modifier = Modifier.padding(top = 8.dp),
        text = stringResource(id = R.string.diary_updateTrack_manageTags_buttonText),
        iconResId = R.drawable.ic_create,
        shape = RoundedCornerShape(32.dp),
        onClick = onManageTags
    )
}