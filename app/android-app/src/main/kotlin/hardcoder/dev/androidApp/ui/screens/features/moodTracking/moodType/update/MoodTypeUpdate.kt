package hardcoder.dev.androidApp.ui.screens.features.moodTracking.moodType.update

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.logic.features.moodTracking.moodType.IncorrectMoodTypeName
import hardcoder.dev.logic.features.moodTracking.moodType.ValidatedMoodTypeName
import hardcoder.dev.logic.icons.LocalIcon
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeUpdateViewModel
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.container.SingleCardSelectionHorizontalGrid
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.slider.IntSlider
import hardcoder.dev.uikit.components.text.Description
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
fun MoodTypeUpdate(
    viewModel: MoodTypeUpdateViewModel,
    onGoBack: () -> Unit,
    onDeleteDialog: (Boolean) -> Unit,
) {
    ScaffoldWrapper(
        content = {
            MoodTypeUpdateContent(
                moodTypeNameController = viewModel.moodTypeNameController,
                iconSelectionController = viewModel.iconSelectionController,
                positiveIndexController = viewModel.positiveIndexController,
                updateController = viewModel.updateController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_moodType_update_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_delete,
                    onActionClick = {
                        onDeleteDialog(true)
                    },
                ),
            ),
        ),
    )
}

@Composable
private fun MoodTypeUpdateContent(
    moodTypeNameController: ValidatedInputController<String, ValidatedMoodTypeName>,
    iconSelectionController: SingleSelectionController<LocalIcon>,
    positiveIndexController: InputController<Int>,
    updateController: RequestController,
) {
    val context = LocalContext.current

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
            EnterMoodTypeNameSection(
                context = context,
                moodTypeNameController = moodTypeNameController,
            )
            Spacer(modifier = Modifier.height(32.dp))
            SelectIconSection(iconSelectionController = iconSelectionController)
            Spacer(modifier = Modifier.height(32.dp))
            EnterMoodTypePositivePercentageSection(positiveIndexController = positiveIndexController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            requestButtonConfig = RequestButtonConfig.Filled(
                controller = updateController,
                iconResId = R.drawable.ic_save,
                labelResId = R.string.moodTracking_moodType_update_createTrack_buttonText,
            ),
        )
    }
}

@Composable
private fun EnterMoodTypeNameSection(
    context: Context,
    moodTypeNameController: ValidatedInputController<String, ValidatedMoodTypeName>,
) {
    Title(text = stringResource(id = R.string.moodTracking_moodType_update_enterName_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        controller = moodTypeNameController,
        modifier = Modifier.fillMaxWidth(),
        label = R.string.moodTracking_moodType_update_enterName_textField,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
        leadingIcon = {
            Icon(
                iconResId = R.drawable.ic_description,
                contentDescription = stringResource(
                    id = R.string.waterTracking_drinkTypes_update_nameIcon_contentDescription,
                ),
            )
        },
        inputAdapter = TextInputAdapter,
        validationAdapter = TextFieldValidationAdapter {
            if (it !is IncorrectMoodTypeName) {
                null
            } else {
                when (val reason = it.reason) {
                    is IncorrectMoodTypeName.Reason.Empty -> {
                        context.getString(R.string.moodTracking_moodType_update_nameEmpty_error)
                    }

                    is IncorrectMoodTypeName.Reason.MoreThanMaxChars -> {
                        context.getString(
                            R.string.moodTracking_moodType_update_nameMoreThanMaxChars_error,
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
    Title(text = stringResource(id = R.string.moodTracking_moodType_update_selectMoodIcon_text))
    Spacer(modifier = Modifier.height(16.dp))
    SingleCardSelectionHorizontalGrid(
        controller = iconSelectionController,
        modifier = Modifier.height(200.dp),
        rows = GridCells.Fixed(count = 3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
        itemContent = { icon, _ ->
            Icon(
                iconResId = icon.resourceId,
                contentDescription = stringResource(id = R.string.waterTracking_drinkTypes_update_drinkTypeIconContentDescription),
                modifier = Modifier
                    .size(60.dp)
                    .padding(12.dp),
            )
        },
    )
}

@Composable
private fun EnterMoodTypePositivePercentageSection(positiveIndexController: InputController<Int>) {
    val state by positiveIndexController.state.collectAsState()

    Title(text = stringResource(id = R.string.moodTracking_moodType_update_selectPositivePercentage_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(
        text = stringResource(
            id = R.string.moodTracking_moodType_update_selectedPositivePercentage_formatText,
            formatArgs = arrayOf(state.input),
        ),
    )
    Spacer(modifier = Modifier.height(16.dp))
    IntSlider(
        selectedValue = state.input,
        onValueChange = positiveIndexController::changeInput,
        valueRange = 10..100,
    )
}