package hardcoder.dev.androidApp.ui.features.diary.create

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
import hardcoder.dev.controller.MultiSelectionController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.features.diary.diaryTrack.IncorrectDiaryTrackContent
import hardcoder.dev.logic.features.diary.diaryTrack.ValidatedDiaryTrackContent
import hardcoder.dev.presentation.features.diary.DiaryCreationViewModel
import hardcoder.dev.uikit.LaunchedEffectWhenExecuted
import hardcoder.dev.uikit.lists.flowRow.MultiSelectionChipFlowRow
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.RequestButtonWithIcon
import hardcoder.dev.uikit.chip.ActionChip
import hardcoder.dev.uikit.chip.content.ChipIconDefaultContent
import hardcoder.dev.uikit.text.Title
import hardcoder.dev.uikit.text.ValidatedTextField
import hardcoder.dev.uikit.text.rememberValidationResourcesAdapter
import hardcoderdev.healther.app.android.app.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun DiaryCreateTrackScreen(
    onGoBack: () -> Unit,
    onManageTags: () -> Unit
) {
    val viewModel = koinViewModel<DiaryCreationViewModel>()

    LaunchedEffectWhenExecuted(controller = viewModel.creationController, action = onGoBack)

    ScaffoldWrapper(
        content = {
            DiaryCreateTrackContent(
                contentController = viewModel.contentController,
                creationController = viewModel.creationController,
                tagMultiSelectionController = viewModel.tagMultiSelectionController,
                onManageTags = onManageTags
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
    contentController: ValidatedInputController<String, ValidatedDiaryTrackContent>,
    creationController: SingleRequestController,
    tagMultiSelectionController: MultiSelectionController<DiaryTag>,
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
            EnterBasicInfoSection(contentController = contentController)
            Spacer(modifier = Modifier.height(32.dp))
            SelectTagsSection(
                tagMultiSelectionController = tagMultiSelectionController,
                onManageTags = onManageTags
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            controller = creationController,
            iconResId = R.drawable.ic_save,
            labelResId = R.string.diary_createTrack_buttonText
        )
    }
}

@Composable
private fun EnterBasicInfoSection(contentController: ValidatedInputController<String, ValidatedDiaryTrackContent>) {
    Title(text = stringResource(id = R.string.diary_createTrack_enterInfo_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        controller = contentController,
        modifier = Modifier.fillMaxWidth(),
        label = R.string.diary_createTrack_enterNote_textField,
        multiline = true,
        minLines = 5,
        maxLines = 5,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default
        ),
        validationAdapter = rememberValidationResourcesAdapter {
            if (it !is IncorrectDiaryTrackContent) null
            else when (it.reason) {
                IncorrectDiaryTrackContent.Reason.Empty -> R.string.diary_createTrack_descriptionEmpty_text
            }
        }
    )
}

@Composable
private fun SelectTagsSection(
    tagMultiSelectionController: MultiSelectionController<DiaryTag>,
    onManageTags: () -> Unit
) {
    Title(text = stringResource(id = R.string.diary_createTrack_youMaySelectTags_text))
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
        text = stringResource(id = R.string.diary_createTrack_manageTags_buttonText),
        iconResId = R.drawable.ic_create,
        shape = RoundedCornerShape(32.dp),
        onClick = onManageTags
    )
}