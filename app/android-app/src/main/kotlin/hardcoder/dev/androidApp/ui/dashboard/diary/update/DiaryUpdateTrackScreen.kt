@file:OptIn(ExperimentalLayoutApi::class)

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
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.IncorrectValidatedDiaryTrackDescription
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.IncorrectValidatedDiaryTrackTitle
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrack
import hardcoder.dev.presentation.dashboard.features.diary.DiaryUpdateTrackViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.IconTextButton
import hardcoder.dev.uikit.chip.Chip
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
                onUpdateText = viewModel::updateText,
                onUpdateTitle = viewModel::updateTitle,
                onUpdateTrack = viewModel::updateTrack,
                onAddTag = viewModel::addTag,
                onRemoveTag = viewModel::removeTag,
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
    onUpdateTitle: (String) -> Unit,
    onUpdateTrack: () -> Unit,
    onAddTag: (DiaryTag) -> Unit,
    onRemoveTag: (DiaryTag) -> Unit,
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
                onUpdateTitle = onUpdateTitle,
                onUpdateText = onUpdateText
            )
            Spacer(modifier = Modifier.height(32.dp))
            state.diaryTrack?.let {
                AttachedEntitySection(diaryTrack = it)
            }
            Spacer(modifier = Modifier.height(32.dp))
            SelectTagsSection(
                state = state,
                onAddTag = onAddTag,
                onRemoveTag = onRemoveTag,
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

@Composable
private fun EnterBasicInfoSection(
    state: DiaryUpdateTrackViewModel.State,
    onUpdateTitle: (String) -> Unit,
    onUpdateText: (String) -> Unit
) {
    val validatedTitle = state.validatedTitle
    val validatedDescription = state.validatedDescription

    Title(text = stringResource(id = R.string.diary_updateTrack_enterInfo_text))
    Spacer(modifier = Modifier.height(16.dp))
    FilledTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.title ?: "",
        onValueChange = onUpdateTitle,
        label = R.string.diary_updateTrack_enterTitle_textField,
        multiline = false,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        isError = validatedTitle is IncorrectValidatedDiaryTrackTitle
    )
    Spacer(modifier = Modifier.height(16.dp))
    AnimatedVisibility(visible = validatedTitle is IncorrectValidatedDiaryTrackTitle) {
        if (validatedTitle is IncorrectValidatedDiaryTrackTitle) {
            ErrorText(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = when (val reason = validatedTitle.reason) {
                    is IncorrectValidatedDiaryTrackTitle.Reason.MoreThanMaxChars -> {
                        stringResource(
                            id = R.string.diary_updateTrack_titleMoreThanMaxChars_text,
                            formatArgs = arrayOf(reason.maxChars)
                        )
                    }

                    else -> {
                        stringResource(id = 0)
                    }
                }
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    FilledTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.description ?: "",
        onValueChange = onUpdateText,
        label = R.string.diary_updateTrack_enterNote_textField,
        multiline = true,
        minLines = 5,
        maxLines = 5,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default
        ),
        isError = validatedDescription is IncorrectValidatedDiaryTrackDescription
    )
    Spacer(modifier = Modifier.height(16.dp))
    AnimatedVisibility(visible = validatedDescription is IncorrectValidatedDiaryTrackDescription) {
        if (validatedDescription is IncorrectValidatedDiaryTrackDescription) {
            ErrorText(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = when (validatedDescription.reason) {
                    is IncorrectValidatedDiaryTrackDescription.Reason.Empty -> {
                        stringResource(R.string.diary_updateTrack_descriptionEmpty_text)
                    }
                }
            )
        }
    }
}

@Composable
private fun AttachedEntitySection(diaryTrack: Any) {
    Title(text = stringResource(id = R.string.diary_updateTrack_attachedEntity_text))
    Spacer(modifier = Modifier.height(16.dp))
    when (diaryTrack) {
        is FastingTrack -> {
            FastingItem(fastingTrack = diaryTrack)
        }

        is MoodTrack -> {
            MoodTrackItem(
                activitiesList = emptyList(),
                moodTrack = diaryTrack,
                onUpdate = {}
            )
        }
    }
}

@Composable
private fun SelectTagsSection(
    state: DiaryUpdateTrackViewModel.State,
    onAddTag: (DiaryTag) -> Unit,
    onRemoveTag: (DiaryTag) -> Unit,
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
            Chip(
                modifier = Modifier.padding(top = 8.dp),
                text = tag.name,
                iconResId = tag.icon.resourceId,
                shape = RoundedCornerShape(32.dp),
                isSelected = state.selectedTags.contains(tag),
                interactionType = InteractionType.SELECTION,
                onClick = {
                    if (state.selectedTags.contains(tag)) {
                        onRemoveTag(tag)
                    } else {
                        onAddTag(tag)
                    }
                }
            )
        }
    }
}

@Composable
private fun ManagementTagsButton(onManageTags: () -> Unit) {
    Chip(
        modifier = Modifier.padding(top = 8.dp),
        text = stringResource(id = R.string.diary_updateTrack_manageTags_buttonText),
        iconResId = R.drawable.ic_create,
        shape = RoundedCornerShape(32.dp),
        interactionType = InteractionType.ACTION,
        onClick = onManageTags
    )
}