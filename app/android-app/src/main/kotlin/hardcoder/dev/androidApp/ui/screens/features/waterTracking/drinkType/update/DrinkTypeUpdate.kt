package hardcoder.dev.androidApp.ui.screens.features.waterTracking.drinkType.update

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.icons.Icon
import hardcoder.dev.icons.resourceId
import hardcoder.dev.logic.features.waterTracking.drinkType.IncorrectDrinkTypeName
import hardcoder.dev.logic.features.waterTracking.drinkType.ValidatedDrinkTypeName
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.IconsMockDataProvider
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
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R

@Composable
fun DrinkTypeUpdate(
    nameInputController: ValidatedInputController<String, ValidatedDrinkTypeName>,
    iconSelectionController: SingleSelectionController<Icon>,
    waterPercentageInputController: InputController<Int>,
    updateController: RequestController,
    onGoBack: () -> Unit,
    onDeleteDialogShow: (Boolean) -> Unit,
) {
    ScaffoldWrapper(
        content = {
            DrinkTypeUpdateContent(
                nameInputController = nameInputController,
                iconSelectionController = iconSelectionController,
                waterPercentageInputController = waterPercentageInputController,
                updateController = updateController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.waterTracking_drinkTypes_update_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_delete,
                    onActionClick = {
                        onDeleteDialogShow(true)
                    },
                ),
            ),
        ),
    )
}

@Composable
private fun DrinkTypeUpdateContent(
    nameInputController: ValidatedInputController<String, ValidatedDrinkTypeName>,
    iconSelectionController: SingleSelectionController<Icon>,
    waterPercentageInputController: InputController<Int>,
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
            EnterDrinkTypeNameSection(context = context, nameInputController = nameInputController)
            Spacer(modifier = Modifier.height(32.dp))
            SelectIconSection(iconSelectionController)
            Spacer(modifier = Modifier.height(32.dp))
            EnterDrinkHydrationIndexPercentageSection(waterPercentageInputController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            requestButtonConfig = RequestButtonConfig.Filled(
                controller = updateController,
                iconResId = R.drawable.ic_save,
                labelResId = R.string.waterTracking_drinkTypes_update_createTrack_buttonText,
            ),
        )
    }
}

@Composable
private fun EnterDrinkTypeNameSection(
    context: Context,
    nameInputController: ValidatedInputController<String, ValidatedDrinkTypeName>,
) {
    Title(text = stringResource(id = R.string.waterTracking_drinkTypes_creation_enterName_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        modifier = Modifier.fillMaxWidth(),
        controller = nameInputController,
        label = R.string.waterTracking_drinkTypes_creation_enterName_textField,
        inputAdapter = TextInputAdapter,
        validationAdapter = TextFieldValidationAdapter {
            if (it !is IncorrectDrinkTypeName) {
                null
            } else {
                when (val reason = it.reason) {
                    is IncorrectDrinkTypeName.Reason.Empty -> {
                        context.getString(R.string.errors_fieldCantBeEmptyError)
                    }

                    is IncorrectDrinkTypeName.Reason.MoreThanMaxChars -> {
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
                iconResId = R.drawable.ic_description,
                contentDescription = stringResource(
                    id = R.string.waterTracking_drinkTypes_creation_nameIcon_contentDescription,
                ),
            )
        },
    )
}

@Composable
private fun SelectIconSection(
    iconSelectionController: SingleSelectionController<Icon>,
) {
    Title(text = stringResource(id = R.string.waterTracking_drinkTypes_creation_selectIcon_text))
    Spacer(modifier = Modifier.height(16.dp))
    SingleCardSelectionHorizontalGrid(
        modifier = Modifier.height(200.dp),
        rows = GridCells.Fixed(count = 3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
        controller = iconSelectionController,
        itemContent = { icon, _ ->
            Icon(
                modifier = Modifier
                    .size(60.dp)
                    .padding(12.dp),
                iconResId = icon.resourceId,
                contentDescription = stringResource(R.string.waterTracking_drinkTypes_creation_drinkTypeIconContentDescription),
            )
        },
    )
}

@Composable
private fun EnterDrinkHydrationIndexPercentageSection(
    waterPercentageInputController: InputController<Int>,
) {
    val state by waterPercentageInputController.state.collectAsState()

    Title(text = stringResource(id = R.string.waterTracking_drinkTypes_creation_selectHydrationIndex_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(
        text = stringResource(
            id = R.string.waterTracking_drinkTypes_creation_selectedIndex_formatText,
            formatArgs = arrayOf(state.input),
        ),
    )
    Spacer(modifier = Modifier.height(16.dp))
    IntSlider(
        controller = waterPercentageInputController,
        valueRange = 30..100,
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun DrinkTypeUpdatePreview() {
    HealtherTheme {
        DrinkTypeUpdate(
            onDeleteDialogShow = {},
            onGoBack = {},
            nameInputController = MockControllersProvider.validatedInputController(""),
            waterPercentageInputController = MockControllersProvider.inputController(0),
            updateController = MockControllersProvider.requestController(),
            iconSelectionController = MockControllersProvider.singleSelectionController(
                dataList = IconsMockDataProvider.icons(),
            ),
        )
    }
}