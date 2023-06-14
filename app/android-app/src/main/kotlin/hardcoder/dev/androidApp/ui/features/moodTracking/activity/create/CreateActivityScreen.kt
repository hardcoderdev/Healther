package hardcoder.dev.androidApp.ui.features.moodTracking.activity.create

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
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.logic.features.moodTracking.activity.IncorrectActivityName
import hardcoder.dev.logic.features.moodTracking.activity.ValidatedActivityName
import hardcoder.dev.logic.icons.LocalIcon
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

@Composable
fun CreateActivityScreen(onGoBack: () -> Unit) {
    val context = LocalContext.current
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel { presentationModule.getCreateActivityViewModel() }

    LaunchedEffectWhenExecuted(controller = viewModel.creationController, action = onGoBack)

    ScaffoldWrapper(
        content = {
            CreateActivityContent(
                context = context,
                activityNameController = viewModel.activityNameController,
                iconSingleSelectionController = viewModel.iconSelectionController,
                creationController = viewModel.creationController
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_createActivity_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun CreateActivityContent(
    context: Context,
    activityNameController: ValidatedInputController<String, ValidatedActivityName>,
    iconSingleSelectionController: SingleSelectionController<LocalIcon>,
    creationController: SingleRequestController
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
            SelectIconSection(iconSingleSelectionController = iconSingleSelectionController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            controller = creationController,
            iconResId = R.drawable.ic_save,
            labelResId = R.string.moodTracking_createActivity_saveTrack_buttonText
        )
    }
}

@Composable
private fun EnterActivityNameSection(
    context: Context,
    activityNameController: ValidatedInputController<String, ValidatedActivityName>
) {
    Title(text = stringResource(id = R.string.moodTracking_createActivity_enter_name_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        modifier = Modifier.fillMaxWidth(),
        multiline = false,
        label = R.string.moodTracking_createActivity_enterActivityName_textField,
        controller = activityNameController,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        validationAdapter = rememberValidationAdapter {
            if (it !is IncorrectActivityName) null
            else when (val reason = it.reason) {
                is IncorrectActivityName.Reason.Empty -> {
                    context.getString(R.string.moodTracking_createActivity_nameEmpty_error)
                }

                is IncorrectActivityName.Reason.MoreThanMaxChars -> {
                    context.getString(
                        R.string.moodTracking_createActivity_nameMoreThanMaxChars_error,
                        reason.maxChars
                    )
                }
            }
        }
    )
}

@Composable
private fun SelectIconSection(iconSingleSelectionController: SingleSelectionController<LocalIcon>) {
    Title(text = stringResource(id = R.string.moodTracking_createActivity_selectIcon_text))
    Spacer(modifier = Modifier.height(16.dp))
    SingleCardSelectionVerticalGrid(
        controller = iconSingleSelectionController,
        columns = GridCells.Adaptive(minSize = 60.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        itemContent = { icon, _ ->
            Icon(
                iconResId = icon.resourceId,
                contentDescription = stringResource(R.string.moodTracking_createActivity_iconContentDescription),
                modifier = Modifier
                    .size(60.dp)
                    .padding(12.dp)
            )
        }
    )
}