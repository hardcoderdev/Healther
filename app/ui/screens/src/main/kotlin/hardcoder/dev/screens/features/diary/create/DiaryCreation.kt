package hardcoder.dev.screens.features.diary.create

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.MultiSelectionController
import hardcoder.dev.entities.features.diary.DiaryTag
import hardcoder.dev.icons.resourceId
import hardcoder.dev.logic.features.diary.diaryTrack.IncorrectDiaryTrackContent
import hardcoder.dev.logic.features.diary.diaryTrack.ValidatedDiaryTrackContent
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.DiaryMockDataProvider
import hardcoder.dev.uikit.components.button.managementButton.ManagementButton
import hardcoder.dev.uikit.components.button.managementButton.ManagementButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.chip.content.ChipIconDefaultContent
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.list.flowRow.MultiSelectionChipFlowRow
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.text.textField.TextInputAdapter
import hardcoder.dev.uikit.components.text.textField.ValidatedTextField
import hardcoder.dev.uikit.components.text.textField.textFieldValidationResourcesAdapter
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun DiaryCreation(
    contentController: ValidatedInputController<String, ValidatedDiaryTrackContent>,
    creationController: RequestController,
    tagMultiSelectionController: MultiSelectionController<DiaryTag>,
    onManageTags: () -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            DiaryCreationContent(
                contentController = contentController,
                creationController = creationController,
                tagMultiSelectionController = tagMultiSelectionController,
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
    creationController: RequestController,
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
                    IncorrectDiaryTrackContent.Reason.Empty -> {
                        R.string.errors_fieldCantBeEmptyError
                    }
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
        verticalArrangement = Arrangement.Center,
        actionButton = {
            ManagementButton(
                managementButtonConfig = ManagementButtonConfig.Chip(
                    titleResId = R.string.diary_tags_title_topBar,
                    iconResId = R.drawable.ic_create,
                    onClick = onManageTags,
                ),
            )
        },
        emptyContent = {
            ManagementButton(
                managementButtonConfig = ManagementButtonConfig.Chip(
                    titleResId = R.string.diary_tags_title_topBar,
                    iconResId = R.drawable.ic_create,
                    onClick = onManageTags,
                ),
            )
        },
        itemContent = { tag, _ ->
            ChipIconDefaultContent(
                iconResId = tag.icon.resourceId,
                name = tag.name,
            )
        },
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun DiaryCreationPreview() {
    HealtherTheme {
        DiaryCreation(
            onGoBack = {},
            onManageTags = {},
            contentController = MockControllersProvider.validatedInputController(""),
            creationController = MockControllersProvider.requestController(),
            tagMultiSelectionController = MockControllersProvider.multiSelectionController(
                dataList = DiaryMockDataProvider.diaryTagsList(
                    context = LocalContext.current,
                ),
            ),
        )
    }
}