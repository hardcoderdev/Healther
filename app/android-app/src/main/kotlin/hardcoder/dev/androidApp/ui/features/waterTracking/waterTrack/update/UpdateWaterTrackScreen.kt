package hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack.update

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.features.DeleteTrackDialog
import hardcoder.dev.androidApp.ui.features.waterTracking.drinkType.DrinkTypeItem
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.androidApp.ui.formatters.RegexHolder
import hardcoder.dev.androidApp.ui.icons.LocalIconImpl
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.logic.features.waterTracking.IncorrectMillilitersCount
import hardcoder.dev.logic.features.waterTracking.ValidatedMillilitersCount
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingUpdateViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.LaunchedEffectWhenExecuted
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.ButtonStyles
import hardcoder.dev.uikit.buttons.ButtonWithIcon
import hardcoder.dev.uikit.buttons.RequestButtonWithIcon
import hardcoder.dev.uikit.chip.ActionChip
import hardcoder.dev.uikit.dialogs.DatePicker
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.ErrorText
import hardcoder.dev.uikit.text.Title
import hardcoder.dev.uikit.text.ValidatedInputField
import hardcoder.dev.uikit.text.rememberInputAdapter
import hardcoder.dev.uikit.text.rememberValidationResourcesAdapter
import hardcoderdev.healther.app.android.app.R
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun UpdateWaterTrackScreen(
    waterTrackId: Int,
    onGoBack: () -> Unit,
    onManageDrinkType: () -> Unit
) {
    val viewModel = koinViewModel<WaterTrackingUpdateViewModel> {
        parametersOf(waterTrackId)
    }

    LaunchedEffectWhenExecuted(viewModel.deletionController, onGoBack)
    LaunchedEffectWhenExecuted(viewModel.updatingController, onGoBack)

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    DeleteTrackDialog(
        dialogOpen = dialogOpen,
        onUpdateDialogOpen = { dialogOpen = it },
        onCancel = { dialogOpen = false },
        onApprove = {
            viewModel.deletionController.request()
            dialogOpen = false
        }
    )

    ScaffoldWrapper(
        content = {
            UpdateWaterTrackContent(
                onManageDrinkType = onManageDrinkType,
                viewModel.dateInputController,
                viewModel.drinkSelectionController,
                viewModel.millilitersDrunkInputController,
                viewModel.updatingController
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.waterTracking_updateWaterTrack_update_title_topBar,
                onGoBack = onGoBack
            )
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_delete,
                    onActionClick = { dialogOpen = true }
                )
            )
        )
    )
}

@Composable
private fun UpdateWaterTrackContent(
    onManageDrinkType: () -> Unit,
    dateInputController: InputController<LocalDateTime>,
    drinkSelectionController: SingleSelectionController<DrinkType>,
    millilitersDrunkInputController: ValidatedInputController<Int, ValidatedMillilitersCount>,
    updatingController: SingleRequestController
) {
    var datePickerDialogVisibility by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            Modifier
                .weight(2f)
                .verticalScroll(rememberScrollState())
        ) {
            EnterDrunkMillilitersSection(millilitersDrunkInputController)
            Spacer(modifier = Modifier.height(32.dp))
            SelectDrinkTypeSection(
                drinkSelectionController = drinkSelectionController,
                onManageDrinkType = onManageDrinkType
            )
            Spacer(modifier = Modifier.height(32.dp))
            SelectDateSection(
                dateInputController = dateInputController,
                onShowDatePicker = { datePickerDialogVisibility = !datePickerDialogVisibility }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            controller = updatingController,
            iconResId = R.drawable.ic_done,
            labelResId = R.string.waterTracking_updateWaterTrack_updateEntry_buttonText,
        )

        DatePicker(
            onUpdateSelectedDate = dateInputController::changeInput,
            isShowing = datePickerDialogVisibility,
            onClose = {
                datePickerDialogVisibility = !datePickerDialogVisibility
            }
        )
    }
}


@Composable
private fun EnterDrunkMillilitersSection(
    millilitersDrunkInputController: ValidatedInputController<Int, ValidatedMillilitersCount>,
) {
    ValidatedInputField(
        modifier = Modifier.fillMaxWidth(),
        controller = millilitersDrunkInputController,
        inputAdapter = rememberInputAdapter(
            decodeInput = { it.toString() },
            encodeInput = { it.toIntOrNull() ?: 0 }
        ),
        validationAdapter = rememberValidationResourcesAdapter {
            if (it !is IncorrectMillilitersCount) null
            else when (it.reason) {
                is IncorrectMillilitersCount.Reason.Empty -> R.string.waterTracking_updateWaterTrack_millilitersEmpty_text
                is IncorrectMillilitersCount.Reason.MoreThanDailyWaterIntake -> R.string.waterTracking_updateWaterTrack_millilitersMoreThanDailyWaterIntake_text
            }
        },
        regex = RegexHolder.textFieldDigitRegex,
        label = R.string.waterTracking_updateWaterTrack_enterMillilitersCountHint_textField,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectDrinkTypeSection(
    drinkSelectionController: SingleSelectionController<DrinkType>,
    onManageDrinkType: () -> Unit
) {
    Title(text = stringResource(id = R.string.waterTracking_updateWaterTrack_enterDrinkType_text))
    Spacer(modifier = Modifier.height(8.dp))

    when (val state = drinkSelectionController.state.collectAsState().value) {
        is SingleSelectionController.State.Loaded -> {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                maxItemsInEachRow = 8
            ) {
                DrinkTypeManagementButton(
                    onManageDrinkType = onManageDrinkType
                )
                state.items.forEach { drink ->
                    DrinkTypeItem(
                        modifier = Modifier.padding(4.dp),
                        drinkType = drink,
                        selectedDrinkType = state.selectedItem,
                        onSelect = { drinkSelectionController.select(drink) }
                    )
                }
            }
        }

        SingleSelectionController.State.Empty -> {
            DrinkTypeManagementButton(
                onManageDrinkType = onManageDrinkType
            )
            Spacer(modifier = Modifier.height(8.dp))
            ErrorText(text = stringResource(id = R.string.waterTracking_createWaterTrack_drinkTypeNotSelected_text))
        }

        SingleSelectionController.State.Loading -> {}
    }
}

@Composable
private fun DrinkTypeManagementButton(onManageDrinkType: () -> Unit) {
    ActionChip(
        modifier = Modifier.padding(4.dp),
        onClick = { onManageDrinkType() },
        text = stringResource(id = R.string.waterTracking_updateWaterTrack_createDrinkType_management_text),
        iconResId = LocalIconImpl(0, R.drawable.ic_create).resourceId,
        shape = RoundedCornerShape(32.dp)
    )
}

@Composable
private fun SelectDateSection(
    dateInputController: InputController<LocalDateTime>,
    onShowDatePicker: () -> Unit
) {
    val state by dateInputController.state.collectAsState()
    val dateTimeFormatter = koinInject<DateTimeFormatter>()
    val selectedDate = state.input.toInstant(TimeZone.currentSystemDefault())
    val formattedDate = dateTimeFormatter.formatDateTime(selectedDate)

    Title(text = stringResource(id = R.string.waterTracking_updateWaterTrack_selectAnotherDate_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(
        text = stringResource(
            id = R.string.waterTracking_updateWaterTrack_selectedDate_formatText,
            formatArgs = arrayOf(formattedDate)
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    ButtonWithIcon(
        iconResId = R.drawable.ic_date_range,
        labelResId = R.string.waterTracking_updateWaterTrack_selectDateRange_buttonText,
        style = ButtonStyles.OUTLINED,
        onClick = onShowDatePicker
    )
}