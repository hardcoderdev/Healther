package hardcoder.dev.screens.features.moodTracking.moodType.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import hardcoder.dev.blocks.components.containers.ScaffoldWrapper
import hardcoder.dev.blocks.components.icon.Icon
import hardcoder.dev.blocks.components.text.Description
import hardcoder.dev.blocks.components.text.Title
import hardcoder.dev.blocks.components.topBar.TopBarConfig
import hardcoder.dev.blocks.components.topBar.TopBarType
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.icons.Icon
import hardcoder.dev.logic.features.moodTracking.moodType.IncorrectMoodTypeName
import hardcoder.dev.logic.features.moodTracking.moodType.ValidatedMoodTypeName
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.IconsMockDataProvider
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.slider.IntSlider
import hardcoder.dev.uikit.components.text.textField.TextFieldValidationAdapter
import hardcoder.dev.uikit.components.text.textField.TextInputAdapter
import hardcoder.dev.uikit.components.text.textField.ValidatedTextField
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.sections.creation.SelectIconSection
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun MoodTypeCreation(
    moodTypeNameController: ValidatedInputController<String, ValidatedMoodTypeName>,
    iconSelectionController: SingleSelectionController<Icon>,
    positiveIndexController: InputController<Int>,
    creationController: RequestController,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            MoodTypeCreationContent(
                moodTypeNameController = moodTypeNameController,
                iconSelectionController = iconSelectionController,
                positiveIndexController = positiveIndexController,
                creationController = creationController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_moodType_creation_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun MoodTypeCreationContent(
    moodTypeNameController: ValidatedInputController<String, ValidatedMoodTypeName>,
    iconSelectionController: SingleSelectionController<Icon>,
    positiveIndexController: InputController<Int>,
    creationController: RequestController,
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
            EnterMoodTypeNameSection(moodTypeNameController = moodTypeNameController)
            Spacer(modifier = Modifier.height(32.dp))
            SelectIconSection(
                titleResId = R.string.moodTracking_moodType_creation_selectIcon_text,
                iconSelectionController = iconSelectionController,
            )
            Spacer(modifier = Modifier.height(32.dp))
            EnterMoodTypePositivePercentageSection(positiveIndexController = positiveIndexController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            requestButtonConfig = RequestButtonConfig.Filled(
                controller = creationController,
                iconResId = R.drawable.ic_save,
                labelResId = R.string.moodTracking_moodType_creation_buttonText,
            ),
        )
    }
}

@Composable
private fun EnterMoodTypeNameSection(
    moodTypeNameController: ValidatedInputController<String, ValidatedMoodTypeName>,
) {
    val context = LocalContext.current

    Title(text = stringResource(id = R.string.moodTracking_moodType_creation_enterName_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        controller = moodTypeNameController,
        modifier = Modifier.fillMaxWidth(),
        label = R.string.moodTracking_moodType_creation_enterName_textField,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
        leadingIcon = {
            Icon(
                iconResId = R.drawable.ic_description,
                contentDescription = stringResource(
                    id = R.string.waterTracking_drinkTypes_creation_nameIcon_contentDescription,
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
                        context.getString(
                            R.string.errors_fieldCantBeEmptyError,
                        )
                    }

                    is IncorrectMoodTypeName.Reason.MoreThanMaxChars -> {
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
private fun EnterMoodTypePositivePercentageSection(positiveIndexController: InputController<Int>) {
    val state by positiveIndexController.state.collectAsState()

    Title(text = stringResource(id = R.string.moodTracking_moodType_creation_selectPositivePercentage_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(
        text = stringResource(
            id = R.string.moodTracking_moodType_creation_selectedIndex_formatText,
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

@HealtherScreenPhonePreviews
@Composable
private fun MoodTypeCreationPreview() {
    HealtherTheme {
        MoodTypeCreation(
            onGoBack = {},
            creationController = MockControllersProvider.requestController(),
            positiveIndexController = MockControllersProvider.inputController(0),
            moodTypeNameController = MockControllersProvider.validatedInputController(""),
            iconSelectionController = MockControllersProvider.singleSelectionController(
                dataList = IconsMockDataProvider.icons(),
            ),
        )
    }
}