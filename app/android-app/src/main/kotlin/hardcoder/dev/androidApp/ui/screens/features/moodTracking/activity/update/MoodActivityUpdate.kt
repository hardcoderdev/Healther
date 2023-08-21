package hardcoder.dev.androidApp.ui.screens.features.moodTracking.activity.update

import android.content.Context

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.logic.features.moodTracking.moodActivity.IncorrectActivityName
import hardcoder.dev.logic.features.moodTracking.moodActivity.ValidatedActivityName
import hardcoder.dev.logic.icons.LocalIcon
import hardcoder.dev.presentation.features.moodTracking.activity.MoodActivityUpdateViewModel
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.container.SingleCardSelectionVerticalGrid
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.text.textField.TextFieldValidationAdapter
import hardcoder.dev.uikit.components.text.textField.TextInputAdapter
import hardcoder.dev.uikit.components.text.textField.ValidatedTextField
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R

@Composable
fun MoodActivityUpdate(
    viewModel: MoodActivityUpdateViewModel,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            MoodActivityUpdateContent(
                activityNameController = viewModel.activityNameController,
                iconSelectionController = viewModel.iconSingleSelectionController,
                updateController = viewModel.updateController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_activity_update_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_delete,
                    onActionClick = viewModel.deleteController::request,
                ),
            ),
        ),
    )
}

@Composable
private fun MoodActivityUpdateContent(
    activityNameController: ValidatedInputController<String, ValidatedActivityName>,
    iconSelectionController: SingleSelectionController<LocalIcon>,
    updateController: RequestController,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(Modifier.weight(2f)) {
            EnterActivityNameSection(
                context = context,
                activityNameController = activityNameController,
            )
            Spacer(modifier = Modifier.height(32.dp))
            SelectIconSection(iconSelectionController = iconSelectionController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            requestButtonConfig = RequestButtonConfig.Filled(
                controller = updateController,
                iconResId = R.drawable.ic_save,
                labelResId = R.string.moodTracking_activity_update_saveTrack_buttonText,
            ),
        )
    }
}

@Composable
private fun EnterActivityNameSection(
    context: Context,
    activityNameController: ValidatedInputController<String, ValidatedActivityName>,
) {
    Title(text = stringResource(id = R.string.moodTracking_activity_update_enter_name_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        controller = activityNameController,
        label = R.string.moodTracking_activity_update_enterActivityName_textField,
        multiline = false,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
        ),
        modifier = Modifier.fillMaxWidth(),
        inputAdapter = TextInputAdapter,
        validationAdapter = TextFieldValidationAdapter {
            if (it !is IncorrectActivityName) {
                null
            } else {
                when (val reason = it.reason) {
                    is IncorrectActivityName.Reason.Empty -> {
                        context.getString(R.string.moodTracking_activity_update_nameEmpty_error)
                    }

                    is IncorrectActivityName.Reason.MoreThanMaxChars -> {
                        context.getString(
                            R.string.moodTracking_activity_update_nameMoreThanMaxChars_error,
                            reason.maxChars,
                        )
                    }
                }
            }
        },
    )
}

@Composable
private fun SelectIconSection(iconSelectionController: SingleSelectionController<LocalIcon>) {
    Title(text = stringResource(id = R.string.moodTracking_activity_update_selectIcon_text))
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
                contentDescription = stringResource(id = R.string.moodTracking_activity_update_activityIconContentDescription),
                modifier = Modifier
                    .size(60.dp)
                    .padding(12.dp),
            )
        },
    )
}