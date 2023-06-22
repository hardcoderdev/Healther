package hardcoder.dev.androidApp.ui.features.diary.tags.create

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
import hardcoder.dev.presentation.features.diary.tags.TagCreationViewModel
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

@Composable
fun CreateTagScreen(onGoBack: () -> Unit) {
    val context = LocalContext.current
    val viewModel = koinViewModel<TagCreationViewModel>()

    LaunchedEffectWhenExecuted(controller = viewModel.creationController, action = onGoBack)

    ScaffoldWrapper(
        content = {
            CreateTagContent(
                context = context,
                tagNameInputController = viewModel.nameInputController,
                iconSelectionController = viewModel.iconSelectionController,
                creationController = viewModel.creationController
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.diary_createTag_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun CreateTagContent(
    context: Context,
    tagNameInputController: ValidatedInputController<String, ValidatedDiaryTagName>,
    iconSelectionController: SingleSelectionController<LocalIcon>,
    creationController: SingleRequestController
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
            controller = creationController,
            iconResId = R.drawable.ic_save,
            labelResId = R.string.diary_createTag_saveTrack_buttonText
        )
    }
}


@Composable
private fun EnterTagNameSection(
    context: Context,
    tagNameInputController: ValidatedInputController<String, ValidatedDiaryTagName>
) {
    Title(text = stringResource(id = R.string.diary_createTag_enter_name_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        controller = tagNameInputController,
        label = R.string.diary_createTag_enterName_textField,
        multiline = false,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier.fillMaxWidth(),
        validationAdapter = rememberValidationAdapter {
            if (it !is IncorrectDiaryTagName) null
            else when (val reason = it.reason) {
                is IncorrectDiaryTagName.Reason.Empty -> {
                    context.getString(R.string.diary_createTag_nameEmpty_error)
                }

                is IncorrectDiaryTagName.Reason.MoreThanMaxChars -> {
                    context.getString(
                        R.string.diary_createTag_nameMoreThanMaxChars_error,
                        reason.maxChars
                    )
                }
            }
        }
    )
}

@Composable
private fun SelectIconSection(iconSelectionController: SingleSelectionController<LocalIcon>) {
    Title(text = stringResource(id = R.string.diary_createTag_selectIcon_text))
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
                contentDescription = stringResource(id = R.string.diary_createTag_tagIconContentDescription)
            )
        }
    )
}