package hardcoder.dev.androidApp.ui.features.moodTracking.activity.update

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
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.logic.features.moodTracking.activity.IncorrectActivityName
import hardcoder.dev.logic.features.moodTracking.activity.ValidatedActivityName
import hardcoder.dev.logic.icons.LocalIcon
import hardcoder.dev.presentation.features.moodTracking.activity.ActivityUpdateViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
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
fun MoodActivityUpdate(
    activityId: Int,
    onGoBack: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = koinViewModel<ActivityUpdateViewModel> {
        parametersOf(activityId)
    }

    LaunchedEffectWhenExecuted(controller = viewModel.updateController, action = onGoBack)
    LaunchedEffectWhenExecuted(controller = viewModel.deleteController, action = onGoBack)

    ScaffoldWrapper(
        content = {
            MoodActivityUpdateContent(
                context = context,
                activityNameController = viewModel.activityNameController,
                iconSelectionController = viewModel.iconSingleSelectionController,
                updateController = viewModel.updateController
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_updateActivity_title_topBar,
                onGoBack = onGoBack
            )
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_delete,
                    onActionClick = viewModel.deleteController::request
                )
            )
        )
    )
}

@Composable
fun MoodActivityUpdateContent(
    context: Context,
    activityNameController: ValidatedInputController<String, ValidatedActivityName>,
    iconSelectionController: SingleSelectionController<LocalIcon>,
    updateController: SingleRequestController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(Modifier.weight(2f)) {
            EnterActivityNameSection(
                context = context,
                activityNameController = activityNameController
            )
            Spacer(modifier = Modifier.height(32.dp))
            SelectIconSection(iconSelectionController = iconSelectionController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            controller = updateController,
            iconResId = R.drawable.ic_save,
            labelResId = R.string.moodTracking_updateActivity_saveTrack_buttonText
        )
    }
}

@Composable
private fun EnterActivityNameSection(
    context: Context,
    activityNameController: ValidatedInputController<String, ValidatedActivityName>
) {
    Title(text = stringResource(id = R.string.moodTracking_updateActivity_enter_name_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        controller = activityNameController,
        label = R.string.moodTracking_updateActivity_enterActivityName_textField,
        multiline = false,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier.fillMaxWidth(),
        validationAdapter = rememberValidationAdapter {
            if (it !is IncorrectActivityName) null
            else when (val reason = it.reason) {
                is IncorrectActivityName.Reason.Empty -> {
                    context.getString(R.string.moodTracking_updateActivity_nameEmpty_error)
                }

                is IncorrectActivityName.Reason.MoreThanMaxChars -> {
                    context.getString(
                        R.string.moodTracking_updateActivity_nameMoreThanMaxChars_error,
                        reason.maxChars
                    )
                }
            }
        }
    )
}

@Composable
private fun SelectIconSection(iconSelectionController: SingleSelectionController<LocalIcon>) {
    Title(text = stringResource(id = R.string.moodTracking_updateActivity_selectIcon_text))
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
                contentDescription = stringResource(id = R.string.moodTracking_updateActivity_activityIconContentDescription),
                modifier = Modifier
                    .size(60.dp)
                    .padding(12.dp)
            )
        }
    )
}