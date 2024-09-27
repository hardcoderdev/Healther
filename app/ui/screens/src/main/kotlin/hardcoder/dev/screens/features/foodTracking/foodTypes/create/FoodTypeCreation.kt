package hardcoder.dev.screens.features.foodTracking.foodTypes.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hardcoder.dev.blocks.components.containers.ScaffoldWrapper
import hardcoder.dev.blocks.components.icon.Icon
import hardcoder.dev.blocks.components.text.Description
import hardcoder.dev.blocks.components.text.Title
import hardcoder.dev.blocks.components.topBar.TopBarConfig
import hardcoder.dev.blocks.components.topBar.TopBarType
import hardcoder.dev.controller.SwitchController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.formatters.RegexHolder
import hardcoder.dev.icons.Icon
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.IconsMockDataProvider
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.text.textField.DecimalInputAdapter
import hardcoder.dev.uikit.components.text.textField.TextField
import hardcoder.dev.uikit.components.text.textField.TextFieldValidationAdapter
import hardcoder.dev.uikit.components.text.textField.TextInputAdapter
import hardcoder.dev.uikit.components.text.textField.ValidatedTextField
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
    spicinessSwitchController: SwitchController,
    vegetarianSwitchController: SwitchController,
    proteinsInputController: InputController<Float>,
    fatsInputController: InputController<Float>,
    carbohydratesInputController: InputController<Float>,
    creationController: RequestController,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            FoodTypeCreationContent(
                nameInputController = nameInputController,
                iconSelectionController = iconSelectionController,
                creationController = creationController,
                spicinessSwitchController = spicinessSwitchController,
                vegetarianSwitchController = vegetarianSwitchController,
                proteinsInputController = proteinsInputController,
                fatsInputController = fatsInputController,
                carbohydratesInputController = carbohydratesInputController,
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
    spicinessSwitchController: SwitchController,
    vegetarianSwitchController: SwitchController,
    proteinsInputController: InputController<Float>,
    fatsInputController: InputController<Float>,
    carbohydratesInputController: InputController<Float>,
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
            Spacer(modifier = Modifier.height(32.dp))
            FoodNutrientsSection(
                proteinsInputController = proteinsInputController,
                fatsInputController = fatsInputController,
                carbohydratesInputController = carbohydratesInputController,
            )
            Spacer(modifier = Modifier.height(32.dp))
            DishFeaturesSection(
                vegetarianSwitchController = vegetarianSwitchController,
                spicinessSwitchController = spicinessSwitchController,
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
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
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

@Composable
private fun DishFeaturesSection(
    spicinessSwitchController: SwitchController,
    vegetarianSwitchController: SwitchController,
) {
    val vegetarianState by vegetarianSwitchController.state.collectAsStateWithLifecycle()
    val spicinessState by spicinessSwitchController.state.collectAsStateWithLifecycle()

    Title(text = stringResource(id = R.string.foodTracking_foodTypes_creation_dish_features_text))
    Spacer(modifier = Modifier.height(16.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = vegetarianState.isEnabled,
            onCheckedChange = { vegetarianSwitchController.toggle() },
        )
        Description(text = stringResource(id = R.string.foodTracking_foodTypes_creation_dish_features_vegetarian_checkbox))
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = spicinessState.isEnabled,
            onCheckedChange = { spicinessSwitchController.toggle() },
        )
        Description(text = stringResource(id = R.string.foodTracking_foodTypes_creation_dish_features_spiciness_checkbox))
    }
}

@Composable
private fun FoodNutrientsSection(
    proteinsInputController: InputController<Float>,
    fatsInputController: InputController<Float>,
    carbohydratesInputController: InputController<Float>,
) {
    Title(text = stringResource(id = R.string.foodTracking_foodTypes_creation_dish_foods_nutrients_text))
    Spacer(modifier = Modifier.height(16.dp))

    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            controller = proteinsInputController,
            inputAdapter = DecimalInputAdapter,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
            label = R.string.foodTracking_foodTypes_creation_dish_proteins_text,
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            controller = fatsInputController,
            inputAdapter = DecimalInputAdapter,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
            label = R.string.foodTracking_foodTypes_creation_dish_fats_text,
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            controller = carbohydratesInputController,
            inputAdapter = DecimalInputAdapter,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
            ),
            label = R.string.foodTracking_foodTypes_creation_dish_carbohydrates_text,
        )
    }
}

@HealtherScreenPhonePreviews
@Composable
private fun FoodTypeCreationPreview() {
    HealtherTheme {
        FoodTypeCreation(
            onGoBack = {},
            nameInputController = MockControllersProvider.validatedInputController(""),
            iconSelectionController = MockControllersProvider.singleSelectionController(dataList = IconsMockDataProvider.icons()),
            spicinessSwitchController = MockControllersProvider.switchController(isActive = true),
            vegetarianSwitchController = MockControllersProvider.switchController(isActive = false),
            proteinsInputController = MockControllersProvider.inputController(4.32f),
            fatsInputController = MockControllersProvider.inputController(2.21f),
            carbohydratesInputController = MockControllersProvider.inputController(3.31f),
            creationController = MockControllersProvider.requestController(),
        )
    }
}