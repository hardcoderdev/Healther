package hardcoder.dev.androidApp.ui.screens.features.moodTracking.activity.update

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
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.icons.Icon
import hardcoder.dev.logic.features.moodTracking.moodActivity.IncorrectActivityName
import hardcoder.dev.logic.features.moodTracking.moodActivity.ValidatedActivityName
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.IconsMockDataProvider
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.text.textField.TextFieldValidationAdapter
import hardcoder.dev.uikit.components.text.textField.TextInputAdapter
import hardcoder.dev.uikit.components.text.textField.ValidatedTextField
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.sections.creation.SelectIconSection
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R

@Composable
fun MoodActivityUpdate(
    activityNameController: ValidatedInputController<String, ValidatedActivityName>,
    iconSingleSelectionController: SingleSelectionController<Icon>,
    updateController: RequestController,
    deleteController: RequestController,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            MoodActivityUpdateContent(
                activityNameController = activityNameController,
                iconSelectionController = iconSingleSelectionController,
                updateController = updateController,
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
                    onActionClick = deleteController::request,
                ),
            ),
        ),
    )
}

@Composable
private fun MoodActivityUpdateContent(
    activityNameController: ValidatedInputController<String, ValidatedActivityName>,
    iconSelectionController: SingleSelectionController<Icon>,
    updateController: RequestController,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(Modifier.weight(2f)) {
            EnterActivityNameSection(activityNameController = activityNameController)
            Spacer(modifier = Modifier.height(32.dp))
            SelectIconSection(
                titleResId = R.string.moodTracking_activity_creation_selectIcon_text,
                iconSelectionController = iconSelectionController,
            )
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
    activityNameController: ValidatedInputController<String, ValidatedActivityName>,
) {
    val context = LocalContext.current

    Title(text = stringResource(id = R.string.moodTracking_activity_creation_enter_name_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        controller = activityNameController,
        label = R.string.moodTracking_activity_creation_enterActivityName_textField,
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
                        context.getString(R.string.errors_fieldCantBeEmptyError)
                    }

                    is IncorrectActivityName.Reason.MoreThanMaxChars -> {
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
private fun MoodActivityUpdatePreview() {
    HealtherTheme {
        MoodActivityUpdate(
            onGoBack = {},
            updateController = MockControllersProvider.requestController(),
            deleteController = MockControllersProvider.requestController(),
            activityNameController = MockControllersProvider.validatedInputController(""),
            iconSingleSelectionController = MockControllersProvider.singleSelectionController(
                dataList = IconsMockDataProvider.icons() + IconsMockDataProvider.icons(),
            ),
        )
    }
}