package hardcoder.dev.screens.features.diary.tags.update

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hardcoder.dev.blocks.components.containers.ScaffoldWrapper
import hardcoder.dev.blocks.components.text.Title
import hardcoder.dev.blocks.components.topBar.TopBarConfig
import hardcoder.dev.blocks.components.topBar.TopBarType
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.icons.Icon
import hardcoder.dev.logic.features.diary.diaryTag.IncorrectDiaryTagName
import hardcoder.dev.logic.features.diary.diaryTag.ValidatedDiaryTagName
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.IconsMockDataProvider
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.text.textField.TextFieldValidationAdapter
import hardcoder.dev.uikit.components.text.textField.TextInputAdapter
import hardcoder.dev.uikit.components.text.textField.ValidatedTextField
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.sections.creation.SelectIconSection
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun DiaryTagUpdate(
    tagNameInputController: ValidatedInputController<String, ValidatedDiaryTagName>,
    iconSelectionController: SingleSelectionController<Icon>,
    updateController: RequestController,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            DiaryTagUpdateContent(
                tagNameInputController = tagNameInputController,
                iconSelectionController = iconSelectionController,
                updateController = updateController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.diary_tag_update_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun DiaryTagUpdateContent(
    tagNameInputController: ValidatedInputController<String, ValidatedDiaryTagName>,
    iconSelectionController: SingleSelectionController<Icon>,
    updateController: RequestController,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(Modifier.weight(2f)) {
            EnterTagNameSection(tagNameInputController = tagNameInputController)
            Spacer(modifier = Modifier.height(32.dp))
            SelectIconSection(
                titleResId = R.string.diary_tag_creation_selectIcon_text,
                iconSelectionController = iconSelectionController,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            requestButtonConfig = RequestButtonConfig.Filled(
                controller = updateController,
                iconResId = R.drawable.ic_save,
                labelResId = R.string.diary_tag_update_saveTrack_buttonText,
            ),
        )
    }
}

@Composable
private fun EnterTagNameSection(
    tagNameInputController: ValidatedInputController<String, ValidatedDiaryTagName>,
) {
    val context = LocalContext.current

    Title(text = stringResource(id = R.string.diary_tag_creation_enter_name_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        controller = tagNameInputController,
        modifier = Modifier.fillMaxWidth(),
        label = R.string.diary_tag_creation_enterName_textField,
        multiline = false,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
        ),
        inputAdapter = TextInputAdapter,
        validationAdapter = TextFieldValidationAdapter {
            if (it !is IncorrectDiaryTagName) {
                null
            } else {
                when (val reason = it.reason) {
                    is IncorrectDiaryTagName.Reason.Empty -> {
                        context.getString(R.string.errors_fieldCantBeEmptyError)
                    }

                    is IncorrectDiaryTagName.Reason.MoreThanMaxChars -> {
                        context.getString(
                            R.string.errors_moreThanMaxCharsError,
                            reason.maxChars,
                        )
                    }
                }
            }
        },
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun DiaryTagUpdatePreview() {
    HealtherTheme {
        DiaryTagUpdate(
            onGoBack = {},
            updateController = MockControllersProvider.requestController(),
            tagNameInputController = MockControllersProvider.validatedInputController(""),
            iconSelectionController = MockControllersProvider.singleSelectionController(
                dataList = IconsMockDataProvider.icons(),
            ),
        )
    }
}