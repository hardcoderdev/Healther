package hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack.create

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.LaunchedEffect
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
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.waterTracking.IncorrectMillilitersInput
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingCreateViewModel
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.ButtonStyles
import hardcoder.dev.uikit.buttons.ButtonWithIcon
import hardcoder.dev.uikit.chip.ActionChip
import hardcoder.dev.uikit.dialogs.DatePicker
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.ErrorText
import hardcoder.dev.uikit.text.FilledTextField
import hardcoder.dev.uikit.text.Title
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
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.value.creationState) {
        if (state.value.creationState is WaterTrackingCreateViewModel.CreationState.Executed) {
            onGoBack()
        }
    }

    ScaffoldWrapper(
        content = {
            CreateWaterTrackContent(
                state = state.value,
                onUpdateWaterDrunk = viewModel::updateWaterDrunk,
                onUpdateSelectedDate = viewModel::updateSelectedDate,
                onUpdateSelectedDrink = viewModel::updateSelectedDrink,
                onCreateWaterTrack = viewModel::createWaterTrack,
                onManageDrinkType = onManageDrinkType
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
    state: WaterTrackingCreateViewModel.State,
    onCreateWaterTrack: () -> Unit,
    onUpdateSelectedDrink: (DrinkType) -> Unit,
    onUpdateSelectedDate: (LocalDateTime) -> Unit,
    onUpdateWaterDrunk: (Int) -> Unit,
    onManageDrinkType: () -> Unit
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
            EnterDrunkMillilitersSection(
                updateWaterDrunk = onUpdateWaterDrunk,
                state = state
            )
            Spacer(modifier = Modifier.height(32.dp))
            SelectDrinkTypeSection(
                state = state,
                updateSelectedDrink = onUpdateSelectedDrink,
                onManageDrinkType = onManageDrinkType
            )
            Spacer(modifier = Modifier.height(32.dp))
            SelectDateSection(
                state = state,
                onShowDatePicker = { datePickerDialogVisibility = !datePickerDialogVisibility }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        ButtonWithIcon(
            iconResId = R.drawable.ic_done,
            labelResId = R.string.waterTracking_createWaterTrack_saveEntry_buttonText,
            onClick = onCreateWaterTrack,
            isEnabled = state.creationAllowed
        )
        DatePicker(
            onUpdateSelectedDate = onUpdateSelectedDate,
            isShowing = datePickerDialogVisibility,
            onClose = {
                datePickerDialogVisibility = !datePickerDialogVisibility
            }
        )
    }
}

@Composable
private fun EnterDrunkMillilitersSection(
    state: WaterTrackingCreateViewModel.State,
    updateWaterDrunk: (Int) -> Unit,
) {
    val validatedMillilitersCount = state.validatedMillilitersCount
    val validatedByRegexMillilitersCount = validatedMillilitersCount?.data ?: 0

    Title(text = stringResource(id = R.string.waterTracking_createWaterTrack_enterMillilitersCount_text))
    Spacer(modifier = Modifier.height(16.dp))
    FilledTextField(
        value = validatedByRegexMillilitersCount.toString(),
        onValueChange = {
            try {
                updateWaterDrunk(it.toInt())
            } catch (e: NumberFormatException) {
                updateWaterDrunk(0)
            }
        },
        regex = RegexHolder.textFieldDigitRegex,
        label = R.string.waterTracking_createWaterTrack_enterMillilitersCountHint_textField,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.fillMaxWidth(),
        isError = state.validatedMillilitersCount is IncorrectMillilitersInput
    )

    AnimatedVisibility(
        visible = validatedMillilitersCount is IncorrectMillilitersInput
    ) {
        if (validatedMillilitersCount is IncorrectMillilitersInput) {
            ErrorText(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = when (validatedMillilitersCount.reason) {
                    is IncorrectMillilitersInput.Reason.Empty -> {
                        stringResource(R.string.waterTracking_createWaterTrack_millilitersEmpty_text)
                    }

                    is IncorrectMillilitersInput.Reason.MoreThanDailyWaterIntake -> {
                        stringResource(R.string.waterTracking_createWaterTrack_millilitersMoreThanDailyWaterIntake_text)
                    }

                    else -> {
                        stringResource(id = 0)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectDrinkTypeSection(
    state: WaterTrackingCreateViewModel.State,
    updateSelectedDrink: (DrinkType) -> Unit,
    onManageDrinkType: () -> Unit
) {
    Title(text = stringResource(id = R.string.waterTracking_createWaterTrack_enterDrinkType_text))
    Spacer(modifier = Modifier.height(8.dp))
    if (state.drinks.isNotEmpty()) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            maxItemsInEachRow = 8
        ) {
            DrinkTypeManagementButton(onManageDrinkType = onManageDrinkType)
            state.drinks.forEach { drink ->
                DrinkTypeItem(
                    modifier = Modifier.padding(4.dp),
                    drinkType = drink,
                    selectedDrinkType = state.selectedDrink,
                    onSelect = updateSelectedDrink
                )
            }
        }
    } else {
        DrinkTypeManagementButton(onManageDrinkType = onManageDrinkType)
        Spacer(modifier = Modifier.height(8.dp))
        ErrorText(text = stringResource(id = R.string.waterTracking_createWaterTrack_drinkTypeNotSelected_text))
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
    state: WaterTrackingCreateViewModel.State,
    onShowDatePicker: () -> Unit
) {
    val uiModule = LocalUIModule.current
    val dateTimeFormatter = uiModule.dateTimeFormatter
    val selectedDate = state.selectedDate.toInstant(TimeZone.currentSystemDefault())
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