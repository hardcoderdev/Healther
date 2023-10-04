package hardcoder.dev.androidApp.ui.screens.features.moodTracking.activity.create

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
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.icons.Icon
import hardcoder.dev.icons.resourceId
import hardcoder.dev.logic.features.moodTracking.moodActivity.IncorrectActivityName
import hardcoder.dev.logic.features.moodTracking.moodActivity.ValidatedActivityName
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.IconsMockDataProvider
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.container.SingleCardSelectionVerticalGrid
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.text.textField.TextFieldValidationAdapter
import hardcoder.dev.uikit.components.text.textField.TextInputAdapter
import hardcoder.dev.uikit.components.text.textField.ValidatedTextField
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R

@Composable
fun MoodActivityCreation(
    activityNameController: ValidatedInputController<String, ValidatedActivityName>,
    iconSingleSelectionController: SingleSelectionController<Icon>,
    creationController: RequestController,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            MoodActivityCreationContent(
                activityNameController = activityNameController,
                iconSingleSelectionController = iconSingleSelectionController,
                creationController = creationController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_activity_creation_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun MoodActivityCreationContent(
    activityNameController: ValidatedInputController<String, ValidatedActivityName>,
    iconSingleSelectionController: SingleSelectionController<Icon>,
    creationController: RequestController,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(Modifier.weight(2f)) {
            EnterActivityNameSection(activityNameController = activityNameController)
            Spacer(modifier = Modifier.height(32.dp))
            SelectIconSection(iconSingleSelectionController = iconSingleSelectionController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            requestButtonConfig = RequestButtonConfig.Filled(
                controller = creationController,
                iconResId = R.drawable.ic_save,
                labelResId = R.string.moodTracking_activity_creation_saveTrack_buttonText,
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
        modifier = Modifier.fillMaxWidth(),
        multiline = false,
        label = R.string.moodTracking_activity_creation_enterActivityName_textField,
        controller = activityNameController,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
        ),
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

@Composable
private fun SelectIconSection(iconSingleSelectionController: SingleSelectionController<Icon>) {
    Title(text = stringResource(id = R.string.moodTracking_activity_creation_selectIcon_text))
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
                contentDescription = stringResource(R.string.moodTracking_activity_creation_iconContentDescription),
                modifier = Modifier
                    .size(60.dp)
                    .padding(12.dp),
            )
        },
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun MoodActivityCreationPreview() {
    HealtherTheme {
        MoodActivityCreation(
            onGoBack = {},
            activityNameController = MockControllersProvider.validatedInputController(""),
            creationController = MockControllersProvider.requestController(),
            iconSingleSelectionController = MockControllersProvider.singleSelectionController(
                dataList = IconsMockDataProvider.icons() + IconsMockDataProvider.icons(),
            ),
        )
    }
}