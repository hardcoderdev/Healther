package hardcoder.dev.androidApp.ui.screens.features.waterTracking.waterTrack.create

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.screens.features.waterTracking.drinkType.DrinkTypeItem
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.coroutines.DefaultBackgroundBackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.formatters.RegexHolder
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType
import hardcoder.dev.logic.features.waterTracking.validators.IncorrectMillilitersCount
import hardcoder.dev.logic.features.waterTracking.validators.ValidatedMillilitersCount
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.mock.dataProviders.features.WaterTrackingMockDataProvider
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.chip.Chip
import hardcoder.dev.uikit.components.chip.ChipConfig
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.text.ErrorText
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.text.textField.TextFieldInputAdapter
import hardcoder.dev.uikit.components.text.textField.ValidatedTextField
import hardcoder.dev.uikit.components.text.textField.textFieldValidationResourcesAdapter
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.sections.dateTime.DateTimeSection
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Composable
fun WaterTrackingCreation(
    dateTimeProvider: DateTimeProvider,
    dateTimeFormatter: DateTimeFormatter,
    millilitersDrunkInputController: ValidatedInputController<Int, ValidatedMillilitersCount>,
    drinkSelectionController: SingleSelectionController<DrinkType>,
    dateInputController: InputController<LocalDate>,
    timeInputController: InputController<LocalTime>,
    creationController: RequestController,
    onManageDrinkTypes: () -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            WaterTrackingCreationContent(
                dateTimeProvider = dateTimeProvider,
                dateTimeFormatter = dateTimeFormatter,
                millilitersDrunkInputController = millilitersDrunkInputController,
                drinkSelectionController = drinkSelectionController,
                dateInputController = dateInputController,
                timeInputController = timeInputController,
                creationController = creationController,
                onManageDrinkTypes = onManageDrinkTypes,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.tracking_creation_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun WaterTrackingCreationContent(
    dateTimeProvider: DateTimeProvider,
    onManageDrinkTypes: () -> Unit,
    dateTimeFormatter: DateTimeFormatter,
    millilitersDrunkInputController: ValidatedInputController<Int, ValidatedMillilitersCount>,
    drinkSelectionController: SingleSelectionController<DrinkType>,
    dateInputController: InputController<LocalDate>,
    timeInputController: InputController<LocalTime>,
    creationController: RequestController,
) {
    Column(Modifier.padding(16.dp)) {
        Column(
            Modifier
                .weight(2f)
                .verticalScroll(rememberScrollState()),
        ) {
            EnterDrunkMillilitersSection(millilitersDrunkInputController)
            Spacer(modifier = Modifier.height(32.dp))
            SelectDrinkTypeSection(
                drinkSelectionController = drinkSelectionController,
                onManageDrinkType = onManageDrinkTypes,
            )
            Spacer(modifier = Modifier.height(32.dp))
            DateTimeSection(
                dateTimeProvider = dateTimeProvider,
                dateTimeFormatter = dateTimeFormatter,
                dateInputController = dateInputController,
                timeInputController = timeInputController,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            requestButtonConfig = RequestButtonConfig.Filled(
                controller = creationController,
                iconResId = R.drawable.ic_done,
                labelResId = R.string.tracking_createEntry_buttonText,
            ),
        )
    }
}

@Composable
private fun EnterDrunkMillilitersSection(
    millilitersDrunkInputController: ValidatedInputController<Int, ValidatedMillilitersCount>,
) {
    Title(text = stringResource(id = R.string.waterTracking_creation_enterMillilitersCount_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        modifier = Modifier.fillMaxWidth(),
        controller = millilitersDrunkInputController,
        inputAdapter = TextFieldInputAdapter(
            decodeInput = { it.toString() },
            encodeInput = { it.toIntOrNull() ?: 0 },
        ),
        validationAdapter = textFieldValidationResourcesAdapter {
            if (it !is IncorrectMillilitersCount) {
                null
            } else {
                when (it.reason) {
                    is IncorrectMillilitersCount.Reason.Empty -> {
                        R.string.errors_fieldCantBeEmptyError
                    }

                    is IncorrectMillilitersCount.Reason.MoreThanDailyWaterIntake -> {
                        R.string.waterTracking_creation_millilitersMoreThanDailyWaterIntake_text
                    }
                }
            }
        },
        regex = RegexHolder.textFieldDigitRegex,
        label = R.string.waterTracking_creation_enterMillilitersCountHint_textField,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectDrinkTypeSection(
    drinkSelectionController: SingleSelectionController<DrinkType>,
    onManageDrinkType: () -> Unit,
) {
    Title(text = stringResource(id = R.string.waterTracking_creation_enterDrinkType_text))
    Spacer(modifier = Modifier.height(8.dp))

    when (val state = drinkSelectionController.state.collectAsState().value) {
        is SingleSelectionController.State.Loaded -> {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                maxItemsInEachRow = 8,
            ) {
                DrinkTypeManagementButton(onManageDrinkType = onManageDrinkType)
                state.items.forEach { drink ->
                    DrinkTypeItem(
                        modifier = Modifier.padding(4.dp),
                        drinkType = drink,
                        selectedDrinkType = state.selectedItem,
                        onSelect = { drinkSelectionController.select(drink) },
                    )
                }
            }
        }

        SingleSelectionController.State.Empty -> {
            DrinkTypeManagementButton(onManageDrinkType = onManageDrinkType)
            Spacer(modifier = Modifier.height(8.dp))
            ErrorText(text = stringResource(id = R.string.waterTracking_creation_drinkTypeNotSelected_text))
        }

        SingleSelectionController.State.Loading -> {}
    }
}

@Composable
private fun DrinkTypeManagementButton(onManageDrinkType: () -> Unit) {
    Chip(
        chipConfig = ChipConfig.Action(
            modifier = Modifier.padding(4.dp),
            onClick = { onManageDrinkType() },
            text = stringResource(id = R.string.waterTracking_drinkTypes_title_topBar),
            iconResId = hardcoder.dev.icons.IconImpl(0, R.drawable.ic_create).resourceId,
            shape = RoundedCornerShape(32.dp),
        ),
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun WaterTrackingCreationPreview() {
    HealtherTheme {
        WaterTrackingCreation(
            onGoBack = {},
            onManageDrinkTypes = {},
            dateTimeProvider = DateTimeProvider(dispatchers = DefaultBackgroundBackgroundCoroutineDispatchers),
            dateTimeFormatter = DateTimeFormatter(LocalContext.current),
            millilitersDrunkInputController = MockControllersProvider.validatedInputController(
                input = 0,
            ),
            dateInputController = MockControllersProvider.inputController(
                input = MockDateProvider.localDate(),
            ),
            timeInputController = MockControllersProvider.inputController(
                input = MockDateProvider.localTime(),
            ),
            creationController = MockControllersProvider.requestController(),
            drinkSelectionController = MockControllersProvider.singleSelectionController(
                dataList = WaterTrackingMockDataProvider.provideDrinkTypesList(
                    context = LocalContext.current,
                ),
            ),
        )
    }
}