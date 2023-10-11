package hardcoder.dev.screens.features.foodTracking.foodTracks

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.coroutines.DefaultBackgroundBackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.entities.features.foodTracking.FoodType
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.icons.IconImpl
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.mock.dataProviders.features.FoodTrackingMockDataProvider
import hardcoder.dev.screens.features.foodTracking.foodTypes.FoodTypeItem
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.chip.Chip
import hardcoder.dev.uikit.components.chip.ChipConfig
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.text.ErrorText
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.text.textField.NumberInputAdapter
import hardcoder.dev.uikit.components.text.textField.TextField
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.sections.dateTime.DateTimeSection
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Composable
fun FoodTrackUpdate(
    caloriesInputController: InputController<Int>,
    dateTimeProvider: DateTimeProvider,
    dateTimeFormatter: DateTimeFormatter,
    foodSelectionController: SingleSelectionController<FoodType>,
    dateInputController: InputController<LocalDate>,
    timeInputController: InputController<LocalTime>,
    updateController: RequestController,
    onManageFoodTypes: () -> Unit,
    onGoBack: () -> Unit,
    onDeleteDialogShow: (Boolean) -> Unit,
) {
    ScaffoldWrapper(
        content = {
            FoodTrackUpdateContent(
                caloriesInputController = caloriesInputController,
                dateTimeProvider = dateTimeProvider,
                dateTimeFormatter = dateTimeFormatter,
                dateInputController = dateInputController,
                timeInputController = timeInputController,
                foodSelectionController = foodSelectionController,
                updateController = updateController,
                onManageFoodTypes = onManageFoodTypes,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.tracking_update_title_topBar,
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
private fun FoodTrackUpdateContent(
    caloriesInputController: InputController<Int>,
    dateTimeProvider: DateTimeProvider,
    dateTimeFormatter: DateTimeFormatter,
    foodSelectionController: SingleSelectionController<FoodType>,
    dateInputController: InputController<LocalDate>,
    timeInputController: InputController<LocalTime>,
    updateController: RequestController,
    onManageFoodTypes: () -> Unit,
) {
    Column(Modifier.padding(16.dp)) {
        Column(
            Modifier
                .weight(2f)
                .verticalScroll(rememberScrollState()),
        ) {
            EnterCaloriesSection(caloriesInputController = caloriesInputController)
            Spacer(modifier = Modifier.height(32.dp))
            SelectFoodTypeSection(
                foodSelectionController = foodSelectionController,
                onManageFoodTypes = onManageFoodTypes,
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
                controller = updateController,
                iconResId = R.drawable.ic_done,
                labelResId = R.string.tracking_updateEntry_buttonText,
            ),
        )
    }
}

@Composable
private fun EnterCaloriesSection(
    caloriesInputController: InputController<Int>,
) {
    Title(text = stringResource(R.string.foodTracking_creation_enterCalories_text))
    Spacer(modifier = Modifier.height(16.dp))
    TextField(
        modifier = Modifier.fillMaxWidth(),
        label = R.string.foodTracking_foodTypes_creation_enterCaloriesCount_text,
        controller = caloriesInputController,
        inputAdapter = NumberInputAdapter,
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectFoodTypeSection(
    foodSelectionController: SingleSelectionController<FoodType>,
    onManageFoodTypes: () -> Unit,
) {
    Title(text = stringResource(id = R.string.foodTracking_creation_enterDrinkType_text))
    Spacer(modifier = Modifier.height(8.dp))

    when (val state = foodSelectionController.state.collectAsState().value) {
        is SingleSelectionController.State.Loaded -> {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                maxItemsInEachRow = 8,
            ) {
                FoodTypeManagementButton(onManageFoodTypes = onManageFoodTypes)
                state.items.forEach { foodType ->
                    FoodTypeItem(
                        modifier = Modifier.padding(4.dp),
                        foodType = foodType,
                        selectedFoodType = state.selectedItem,
                        onSelect = { foodSelectionController.select(foodType) },
                    )
                }
            }
        }

        SingleSelectionController.State.Empty -> {
            FoodTypeManagementButton(onManageFoodTypes = onManageFoodTypes)
            Spacer(modifier = Modifier.height(8.dp))
            ErrorText(text = stringResource(id = R.string.foodTracking_creation_foodTypeNotSelected_text))
        }

        SingleSelectionController.State.Loading -> {}
    }
}

@Composable
private fun FoodTypeManagementButton(onManageFoodTypes: () -> Unit) {
    Chip(
        chipConfig = ChipConfig.Action(
            modifier = Modifier.padding(4.dp),
            onClick = onManageFoodTypes,
            text = stringResource(id = R.string.foodTracking_foodTypes_title_topBar),
            iconResId = IconImpl(0, R.drawable.ic_create).resourceId,
            shape = RoundedCornerShape(32.dp),
        ),
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun FoodTrackUpdatePreview() {
    HealtherTheme {
        FoodTrackUpdate(
            onGoBack = {},
            onManageFoodTypes = {},
            onDeleteDialogShow = {},
            caloriesInputController = MockControllersProvider.inputController(0),
            dateTimeProvider = DateTimeProvider(dispatchers = DefaultBackgroundBackgroundCoroutineDispatchers),
            dateTimeFormatter = DateTimeFormatter(context = LocalContext.current),
            dateInputController = MockControllersProvider.inputController(MockDateProvider.localDate()),
            timeInputController = MockControllersProvider.inputController(MockDateProvider.localTime()),
            updateController = MockControllersProvider.requestController(),
            foodSelectionController = MockControllersProvider.singleSelectionController(
                dataList = FoodTrackingMockDataProvider.foodTypesList(),
            ),
        )
    }
}