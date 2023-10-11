@file:OptIn(ExperimentalComposeUiApi::class)

package hardcoder.dev.screens.features.foodTracking.foodTypes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.icons.Icon
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.IconsMockDataProvider
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.text.textField.TextFieldValidationAdapter
import hardcoder.dev.uikit.components.text.textField.TextInputAdapter
import hardcoder.dev.uikit.components.text.textField.ValidatedTextField
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.sections.creation.SelectIconSection
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoder.dev.validators.features.foodTracking.IncorrectFoodTypeName
import hardcoder.dev.validators.features.foodTracking.ValidatedFoodTypeName
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun FoodTypeCreation(
    nameInputController: ValidatedInputController<String, ValidatedFoodTypeName>,
    iconSelectionController: SingleSelectionController<Icon>,
    creationController: RequestController,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            FoodTypeCreationContent(
                nameInputController = nameInputController,
                iconSelectionController = iconSelectionController,
                creationController = creationController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.foodTracking_foodTypes_creation_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun FoodTypeCreationContent(
    nameInputController: ValidatedInputController<String, ValidatedFoodTypeName>,
    iconSelectionController: SingleSelectionController<Icon>,
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
            EnterNameSection(nameInputController = nameInputController)
            Spacer(modifier = Modifier.height(32.dp))
            SelectIconSection(
                titleResId = R.string.foodTracking_foodTypes_creation_selectIcon_text,
                iconSelectionController = iconSelectionController,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            requestButtonConfig = RequestButtonConfig.Filled(
                controller = creationController,
                iconResId = R.drawable.ic_save,
                labelResId = R.string.tracking_createEntry_buttonText,
            ),
        )
    }
}

@Composable
private fun EnterNameSection(
    nameInputController: ValidatedInputController<String, ValidatedFoodTypeName>,
) {
    val context = LocalContext.current

    Title(text = stringResource(id = R.string.foodTracking_foodTypes_creation_enterMainInfo_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = R.string.foodTracking_foodTypes_creation_enterName_text,
        controller = nameInputController,
        inputAdapter = TextInputAdapter,
        validationAdapter = TextFieldValidationAdapter {
            if (it !is IncorrectFoodTypeName) {
                null
            } else {
                when (val reason = it.reason) {
                    is IncorrectFoodTypeName.Reason.Empty -> {
                        context.getString(R.string.errors_fieldCantBeEmptyError)
                    }

                    is IncorrectFoodTypeName.Reason.MoreThanMaxChars -> {
                        context.getString(
                            R.string.errors_moreThanMaxCharsError,
                            reason.maxChars,
                        )
                    }
                }
            }
        },
        leadingIcon = {
            Icon(
                iconResId = R.drawable.ic_create,
                contentDescription = stringResource(
                    id = R.string.waterTracking_drinkTypes_creation_nameIcon_contentDescription,
                ),
            )
        },
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun FoodTypeCreationPreview() {
    HealtherTheme {
        FoodTypeCreation(
            onGoBack = {},
            nameInputController = MockControllersProvider.validatedInputController(""),
            iconSelectionController = MockControllersProvider.singleSelectionController(dataList = IconsMockDataProvider.icons()),
            creationController = MockControllersProvider.requestController(),
        )
    }
}