package hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack.create

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
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.di.LocalUIModule
import hardcoder.dev.androidApp.ui.features.waterTracking.drinkType.DrinkTypeItem
import hardcoder.dev.androidApp.ui.formatters.RegexHolder
import hardcoder.dev.androidApp.ui.icons.LocalIconImpl
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.waterTracking.IncorrectMillilitersCount
import hardcoder.dev.logic.features.waterTracking.ValidatedMillilitersCount
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType
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
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Composable
fun CreateWaterTrackScreen(
    onGoBack: () -> Unit,
    onManageDrinkType: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getWaterTrackCreateViewModel()
    }


    LaunchedEffectWhenExecuted(viewModel.creationController, onGoBack)

    ScaffoldWrapper(
        content = {
            CreateWaterTrackContent(
                onManageDrinkType = onManageDrinkType,
                millilitersDrunkInputController = viewModel.millilitersDrunkInputController,
                drinkSelectionController = viewModel.drinkSelectionController,
                dateInputController = viewModel.dateInputController,
                creationController = viewModel.creationController
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.waterTracking_createWaterTrack_create_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun CreateWaterTrackContent(
    onManageDrinkType: () -> Unit,
    millilitersDrunkInputController: ValidatedInputController<Int, ValidatedMillilitersCount>,
    drinkSelectionController: SingleSelectionController<DrinkType>,
    dateInputController: InputController<LocalDateTime>,
    creationController: SingleRequestController
) {
    var datePickerDialogVisibility by remember {
        mutableStateOf(false)
    }

    Column(Modifier.padding(16.dp)) {
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
            controller = creationController,
            iconResId = R.drawable.ic_done,
            labelResId = R.string.waterTracking_createWaterTrack_saveEntry_buttonText,
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
    Title(text = stringResource(id = R.string.waterTracking_createWaterTrack_enterMillilitersCount_text))
    Spacer(modifier = Modifier.height(16.dp))
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
                is IncorrectMillilitersCount.Reason.Empty -> R.string.waterTracking_createWaterTrack_millilitersEmpty_text
                is IncorrectMillilitersCount.Reason.MoreThanDailyWaterIntake -> R.string.waterTracking_createWaterTrack_millilitersMoreThanDailyWaterIntake_text
            }
        },
        regex = RegexHolder.textFieldDigitRegex,
        label = R.string.waterTracking_createWaterTrack_enterMillilitersCountHint_textField,
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
    Title(text = stringResource(id = R.string.waterTracking_createWaterTrack_enterDrinkType_text))
    Spacer(modifier = Modifier.height(8.dp))

    when (val state = drinkSelectionController.state.collectAsState().value) {
        is SingleSelectionController.State.Loaded -> {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                maxItemsInEachRow = 8
            ) {
                DrinkTypeManagementButton(onManageDrinkType = onManageDrinkType)
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
            DrinkTypeManagementButton(onManageDrinkType = onManageDrinkType)
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
        text = stringResource(id = R.string.waterTracking_createWaterTrack_createDrinkType_management_text),
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
    val uiModule = LocalUIModule.current
    val dateTimeFormatter = uiModule.dateTimeFormatter
    val selectedDate = state.input.toInstant(TimeZone.currentSystemDefault())
    val formattedDate = dateTimeFormatter.formatDateTime(selectedDate)

    Title(text = stringResource(id = R.string.waterTracking_createWaterTrack_selectAnotherDate_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(
        text = stringResource(
            id = R.string.waterTracking_createWaterTrack_selectedDate_formatText,
            formatArgs = arrayOf(formattedDate)
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    ButtonWithIcon(
        iconResId = R.drawable.ic_date_range,
        labelResId = R.string.waterTracking_createWaterTrack_selectDateRange_buttonText,
        style = ButtonStyles.OUTLINED,
        onClick = onShowDatePicker
    )
}