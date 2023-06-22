package hardcoder.dev.androidApp.ui.features.diary.tags.update

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.logic.features.diary.diaryTag.IncorrectDiaryTagName
import hardcoder.dev.logic.features.diary.diaryTag.ValidatedDiaryTagName
import hardcoder.dev.logic.icons.LocalIcon
import hardcoder.dev.presentation.features.diary.tags.TagUpdateViewModel
import hardcoder.dev.uikit.LaunchedEffectWhenExecuted
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.SingleCardSelectionVerticalGrid
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.RequestButtonWithIcon
import hardcoder.dev.uikit.icons.Icon
import hardcoder.dev.uikit.text.Title
import hardcoder.dev.uikit.text.ValidatedTextField
import hardcoder.dev.uikit.text.rememberValidationAdapter
import hardcoderdev.healther.app.android.app.R
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DiaryTagUpdate(
    tagId: Int,
    onGoBack: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = koinViewModel<TagUpdateViewModel> {
        parametersOf(tagId)
    }

    LaunchedEffectWhenExecuted(controller = viewModel.updateController, action = onGoBack)
    LaunchedEffectWhenExecuted(controller = viewModel.deleteController, action = onGoBack)

    ScaffoldWrapper(
        content = {
            DiaryTagUpdateContent(
                context = context,
                tagNameInputController = viewModel.tagNameInputController,
                iconSelectionController = viewModel.iconSelectionController,
                updateController = viewModel.updateController
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.diary_updateTag_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun DiaryTagUpdateContent(
    context: Context,
    tagNameInputController: ValidatedInputController<String, ValidatedDiaryTagName>,
    iconSelectionController: SingleSelectionController<LocalIcon>,
    updateController: SingleRequestController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(Modifier.weight(2f)) {
            EnterTagNameSection(context = context, tagNameInputController = tagNameInputController)
            Spacer(modifier = Modifier.height(32.dp))
            SelectIconSection(iconSelectionController = iconSelectionController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            controller = updateController,
            iconResId = R.drawable.ic_save,
            labelResId = R.string.diary_updateTag_saveTrack_buttonText
        )
    }
}

@Composable
private fun EnterTagNameSection(
    context: Context,
    tagNameInputController: ValidatedInputController<String, ValidatedDiaryTagName>
) {
    Title(text = stringResource(id = R.string.diary_updateTag_enter_name_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        controller = tagNameInputController,
        modifier = Modifier.fillMaxWidth(),
        label = R.string.diary_updateTag_enterName_textField,
        multiline = false,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        validationAdapter = rememberValidationAdapter {
            if (it !is IncorrectDiaryTagName) null
            else when (val reason = it.reason) {
                is IncorrectDiaryTagName.Reason.Empty -> {
                    context.getString(R.string.diary_updateTag_nameEmpty_error)
                }

                is IncorrectDiaryTagName.Reason.MoreThanMaxChars -> {
                    context.getString(
                        R.string.diary_updateTag_nameMoreThanMaxChars_error,
                        reason.maxChars
                    )
                }
            }
        }
    )
}

@Composable
private fun SelectIconSection(iconSelectionController: SingleSelectionController<LocalIcon>) {
    Title(text = stringResource(id = R.string.diary_updateTag_selectIcon_text))
    Spacer(modifier = Modifier.height(16.dp))
    SingleCardSelectionVerticalGrid(
        controller = iconSelectionController,
        columns = GridCells.Adaptive(minSize = 60.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        itemContent = { icon, _ ->
            Icon(
                iconResId = icon.resourceId,
                contentDescription = stringResource(id = R.string.diary_updateTag_tagIconContentDescription)
            )
        }
    )
}