@file:OptIn(ExperimentalLayoutApi::class)

package hardcoder.dev.androidApp.ui.dashboard.diary

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
import hardcoder.dev.healther.R
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.IncorrectValidatedDiaryTrackDescription
import hardcoder.dev.logic.dashboard.features.diary.featureTag.FeatureTag
import hardcoder.dev.presentation.dashboard.features.diary.DiaryCreateTrackViewModel
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
fun DiaryCreateTrackScreen(onGoBack: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getDiaryCreateTrackViewModel()
    }
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.value.creationState) {
        if (state.value.creationState is DiaryCreateTrackViewModel.CreationState.Executed) {
            onGoBack()
        }
    }

    ScaffoldWrapper(
        content = {
            DiaryCreateTrackContent(
                state = state.value,
                onUpdateText = viewModel::updateText,
                onUpdateTitle = viewModel::updateTitle,
                onRemoveFeatureTag = viewModel::removeFeatureTag,
                onAddFeatureTag = viewModel::addFeatureTag,
                onCreateTrack = viewModel::createTrack
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.diary_createTrack_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun DiaryCreateTrackContent(
    state: DiaryCreateTrackViewModel.State,
    onUpdateText: (String) -> Unit,
    onUpdateTitle: (String) -> Unit,
    onRemoveFeatureTag: (FeatureTag) -> Unit,
    onAddFeatureTag: (FeatureTag) -> Unit,
    onCreateTrack: () -> Unit
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
            FeatureTagSection(
                state = state,
                onRemoveFeatureTag = onRemoveFeatureTag,
                onAddFeatureTag = onAddFeatureTag
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconTextButton(
            iconResId = R.drawable.ic_save,
            labelResId = R.string.diary_createTrack_buttonText,
            onClick = onCreateTrack,
            isEnabled = state.creationAllowed
        )
    }
}

@Composable
private fun EnterBasicInfoSection(
    state: DiaryCreateTrackViewModel.State,
    onUpdateTitle: (String) -> Unit,
    onUpdateText: (String) -> Unit
) {
    val validatedDescription = state.validatedDiaryTrackDescription

    Title(text = stringResource(id = R.string.diary_createTrack_enterInfo_text))
    Spacer(modifier = Modifier.height(16.dp))
    FilledTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.title ?: "",
        onValueChange = onUpdateTitle,
        label = R.string.diary_createTrack_enterTitle_textField,
        multiline = false,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    FilledTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.description ?: "",
        onValueChange = onUpdateText,
        label = R.string.diary_createTrack_enterNote_textField,
        multiline = true,
        minLines = 5,
        maxLines = 5,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    AnimatedVisibility(visible = validatedDescription is IncorrectValidatedDiaryTrackDescription) {
        if (validatedDescription is IncorrectValidatedDiaryTrackDescription) {
            ErrorText(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = when (validatedDescription.reason) {
                    is IncorrectValidatedDiaryTrackDescription.Reason.Empty -> {
                        stringResource(R.string.diary_createTrack_descriptionEmpty_text)
                    }

                    else -> {
                        stringResource(id = 0)
                    }
                }
            )
        }
    }
}

@Composable
private fun FeatureTagSection(
    state: DiaryCreateTrackViewModel.State,
    onRemoveFeatureTag: (FeatureTag) -> Unit,
    onAddFeatureTag: (FeatureTag) -> Unit
) {
    Title(text = stringResource(id = R.string.diary_createTrack_selectLinkedFeatureType_text))
    Spacer(modifier = Modifier.height(16.dp))
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        maxItemsInEachRow = 4
    ) {
        state.featureTagList.forEach { featureTag ->
            Chip(
                modifier = Modifier.padding(bottom = 8.dp),
                text = featureTag.name,
                shape = RoundedCornerShape(8.dp),
                isSelected = state.selectedFeatureTags.contains(featureTag),
                interactionType = InteractionType.SELECTION,
                onClick = {
                    if (state.selectedFeatureTags.contains(featureTag)) {
                        onRemoveFeatureTag(featureTag)
                    } else {
                        onAddFeatureTag(featureTag)
                    }
                }
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    AnimatedVisibility(visible = state.selectedFeatureTags.isEmpty()) {
        ErrorText(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
            text = stringResource(R.string.diary_createTrack_featureTagNotSelected_text)
        )
    }
}