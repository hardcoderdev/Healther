package hardcoder.dev.androidApp.ui.screens.features.diary.create

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.SingleRequestController
import hardcoder.dev.controller.selection.MultiSelectionController
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.features.diary.diaryTrack.IncorrectDiaryTrackContent
import hardcoder.dev.logic.features.diary.diaryTrack.ValidatedDiaryTrackContent
import hardcoder.dev.presentation.features.diary.DiaryCreationViewModel
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.chip.Chip
import hardcoder.dev.uikit.components.chip.ChipConfig
import hardcoder.dev.uikit.components.chip.content.ChipIconDefaultContent
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.list.flowRow.MultiSelectionChipFlowRow
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.text.textField.TextInputAdapter
import hardcoder.dev.uikit.components.text.textField.ValidatedTextField
import hardcoder.dev.uikit.components.text.textField.textFieldValidationResourcesAdapter
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R

@Composable
fun DiaryCreation(
    viewModel: DiaryCreationViewModel,
    onManageTags: () -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            DiaryCreationContent(
                contentController = viewModel.contentController,
                creationController = viewModel.creationController,
                tagMultiSelectionController = viewModel.tagMultiSelectionController,
                onManageTags = onManageTags,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.diary_creation_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun DiaryCreationContent(
    contentController: ValidatedInputController<String, ValidatedDiaryTrackContent>,
    creationController: SingleRequestController,
    tagMultiSelectionController: MultiSelectionController<DiaryTag>,
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
            EnterBasicInfoSection(contentController = contentController)
            Spacer(modifier = Modifier.height(32.dp))
            SelectTagsSection(
                tagMultiSelectionController = tagMultiSelectionController,
                onManageTags = onManageTags,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            requestButtonConfig = RequestButtonConfig.Filled(
                controller = creationController,
                iconResId = R.drawable.ic_save,
                labelResId = R.string.diary_creation_buttonText,
            ),
        )
    }
}

@Composable
private fun EnterBasicInfoSection(contentController: ValidatedInputController<String, ValidatedDiaryTrackContent>) {
    Title(text = stringResource(id = R.string.diary_creation_enterInfo_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        controller = contentController,
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
        validationAdapter = textFieldValidationResourcesAdapter {
            if (it !is IncorrectDiaryTrackContent) {
                null
            } else {
                when (it.reason) {
                    IncorrectDiaryTrackContent.Reason.Empty -> R.string.diary_creation_descriptionEmpty_text
                }
            }
        },
    )
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
        verticalAlignment = Alignment.CenterVertically,
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
            text = stringResource(id = R.string.diary_creation_manageTags_buttonText),
            iconResId = R.drawable.ic_create,
            shape = RoundedCornerShape(32.dp),
            onClick = onManageTags,
        ),
    )
}