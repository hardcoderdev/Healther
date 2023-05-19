package hardcoder.dev.androidApp.ui.dashboard.diary.update

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.ui.features.fasting.FastingItem
import hardcoder.dev.androidApp.ui.features.moodTracking.MoodTrackItem
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.healther.R
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentGroup
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.IncorrectDiaryTrackContent
import hardcoder.dev.presentation.dashboard.features.diary.DiaryUpdateTrackViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.IconTextButton
import hardcoder.dev.uikit.chip.ActionChip
import hardcoder.dev.uikit.chip.SelectionChip
import hardcoder.dev.uikit.text.ErrorText
import hardcoder.dev.uikit.text.FilledTextField
import hardcoder.dev.uikit.text.Title

@Composable
fun DiaryUpdateTrackScreen(
    diaryTrackId: Int,
    onGoBack: () -> Unit,
    onManageTags: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getDiaryUpdateTrackViewModel(diaryTrackId)
    }
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.value.updateState) {
        if (state.value.updateState is DiaryUpdateTrackViewModel.UpdateState.Executed) {
            onGoBack()
        }
    }

    LaunchedEffect(key1 = state.value.deleteState) {
        if (state.value.deleteState is DiaryUpdateTrackViewModel.DeleteState.Executed) {
            onGoBack()
        }
    }

    ScaffoldWrapper(
        content = {
            DiaryUpdateTrackContent(
                state = state.value,
                onUpdateText = viewModel::updateContent,
                onUpdateTrack = viewModel::updateTrack,
                onToggleTag = viewModel::toggleTag,
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
                    onActionClick = viewModel::deleteTrackById
                )
            )
        )
    )
}

@Composable
private fun DiaryUpdateTrackContent(
    state: DiaryUpdateTrackViewModel.State,
    onUpdateText: (String) -> Unit,
    onUpdateTrack: () -> Unit,
    onToggleTag: (DiaryTag) -> Unit,
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
                state = state,
                onUpdateText = onUpdateText
            )
            if (state.diaryAttachmentGroup.isAttachmentTracksNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                AttachedEntitySection(diaryAttachmentGroup = state.diaryAttachmentGroup)
            }
            Spacer(modifier = Modifier.height(32.dp))
            SelectTagsSection(
                state = state,
                onToggleTag = onToggleTag,
                onManageTags = onManageTags
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconTextButton(
            iconResId = R.drawable.ic_save,
            labelResId = R.string.diary_createTrack_buttonText,
            onClick = onUpdateTrack,
            isEnabled = state.updateAllowed
        )
    }
}

private fun DiaryAttachmentGroup.isAttachmentTracksNotEmpty() =
    fastingTracks.isNotEmpty() || moodTracks.isNotEmpty()

@Composable
private fun EnterBasicInfoSection(
    state: DiaryUpdateTrackViewModel.State,
    onUpdateText: (String) -> Unit
) {
    val validatedDescription = state.validatedContent

    Title(text = stringResource(id = R.string.diary_updateTrack_enterInfo_text))
    Spacer(modifier = Modifier.height(16.dp))
    FilledTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.content ?: "",
        onValueChange = onUpdateText,
        label = R.string.diary_updateTrack_enterNote_textField,
        multiline = true,
        minLines = 5,
        maxLines = 5,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default
        ),
        isError = validatedDescription is IncorrectDiaryTrackContent
    )
    Spacer(modifier = Modifier.height(16.dp))
    AnimatedVisibility(visible = validatedDescription is IncorrectDiaryTrackContent) {
        if (validatedDescription is IncorrectDiaryTrackContent) {
            ErrorText(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = when (validatedDescription.reason) {
                    is IncorrectDiaryTrackContent.Reason.Empty -> {
                        stringResource(R.string.diary_updateTrack_descriptionEmpty_text)
                    }
                }
            )
        }
    }
}

@Composable
private fun AttachedEntitySection(diaryAttachmentGroup: DiaryAttachmentGroup) {
    Title(text = stringResource(id = R.string.diary_updateTrack_attachedEntity_text))
    Spacer(modifier = Modifier.height(16.dp))
    diaryAttachmentGroup.fastingTracks.forEach { fastingTrack ->
        FastingItem(fastingTrack = fastingTrack)
    }
    diaryAttachmentGroup.moodTracks.forEach { moodTrack ->
        MoodTrackItem(
            activitiesList = emptyList(),
            moodTrack = moodTrack,
            onUpdate = {}
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectTagsSection(
    state: DiaryUpdateTrackViewModel.State,
    onToggleTag: (DiaryTag) -> Unit,
    onManageTags: () -> Unit
) {
    Title(text = stringResource(id = R.string.diary_updateTrack_youMaySelectTags_text))
    Spacer(modifier = Modifier.height(16.dp))
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        maxItemsInEachRow = 6
    ) {
        ManagementTagsButton(onManageTags = onManageTags)
        state.tagList.forEach { tag ->
            SelectionChip(
                modifier = Modifier.padding(top = 8.dp),
                text = tag.name,
                iconResId = tag.icon.resourceId,
                shape = RoundedCornerShape(32.dp),
                isSelected = state.diaryAttachmentGroup.tags.contains(tag),
                onClick = { onToggleTag(tag) }
            )
        }
    }
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