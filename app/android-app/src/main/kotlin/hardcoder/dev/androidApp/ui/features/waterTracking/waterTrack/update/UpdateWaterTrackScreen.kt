@file:OptIn(ExperimentalLayoutApi::class)

package hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack.update

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
import hardcoder.dev.androidApp.ui.features.DeleteTrackDialog
import hardcoder.dev.androidApp.ui.features.waterTracking.drinkType.DrinkTypeItem
import hardcoder.dev.androidApp.ui.formatters.RegexHolder
import hardcoder.dev.androidApp.ui.icons.LocalIconImpl
import hardcoder.dev.extensions.toDate
import hardcoder.dev.healther.R
import hardcoder.dev.logic.entities.features.waterTracking.DrinkType
import hardcoder.dev.logic.features.waterTracking.IncorrectMillilitersInput
import hardcoder.dev.presentation.features.waterTracking.WaterTrackUpdateViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.ButtonStyles
import hardcoder.dev.uikit.buttons.IconTextButton
import hardcoder.dev.uikit.dialogs.DatePicker
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.ErrorText
import hardcoder.dev.uikit.text.FilledTextField
import hardcoder.dev.uikit.text.Title
import kotlinx.datetime.LocalDateTime
import java.text.DateFormat

@Composable
fun UpdateWaterTrackScreen(
    waterTrackId: Int,
    onGoBack: () -> Unit,
    onManageDrinkType: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getWaterTrackUpdateViewModel(waterTrackId)
    }
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.value.updateState) {
        if (state.value.updateState is WaterTrackUpdateViewModel.UpdateState.Executed) {
            onGoBack()
        }
    }

    LaunchedEffect(key1 = state.value.deleteState) {
        if (state.value.deleteState is WaterTrackUpdateViewModel.DeleteState.Executed) {
            onGoBack()
        }
    }

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    DeleteTrackDialog(
        dialogOpen = dialogOpen,
        onUpdateDialogOpen = { dialogOpen = it },
        onApprove = viewModel::deleteWaterTrack,
        onCancel = { dialogOpen = false }
    )

    ScaffoldWrapper(
        content = {
            UpdateWaterTrackContent(
                state = state.value,
                onUpdateWaterDrunk = viewModel::updateWaterDrunk,
                onUpdateSelectedDate = viewModel::updateSelectedDate,
                onUpdateSelectedDrink = viewModel::updateSelectedDrink,
                onUpdateWaterTrack = viewModel::updateWaterTrack,
                onManageDrinkType = onManageDrinkType
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.waterTracking_UpdateWaterTrack_update_title_topBar,
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
    state: WaterTrackUpdateViewModel.State,
    onUpdateWaterTrack: () -> Unit,
    onUpdateSelectedDrink: (DrinkType) -> Unit,
    onUpdateSelectedDate: (LocalDateTime) -> Unit,
    onUpdateWaterDrunk: (Int) -> Unit,
    onManageDrinkType: () -> Unit
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
        IconTextButton(
            iconResId = R.drawable.ic_done,
            labelResId = R.string.waterTracking_UpdateWaterTrack_updateEntry_buttonText,
            onClick = onUpdateWaterTrack,
            isEnabled = state.allowUpdate
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
    state: WaterTrackUpdateViewModel.State,
    updateWaterDrunk: (Int) -> Unit,
) {
    val validatedMillilitersCount = state.validatedMillilitersCount
    val validatedByRegexMillilitersCount = validatedMillilitersCount?.data ?: 0

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
        label = R.string.waterTracking_UpdateWaterTrack_enterMillilitersCountHint_textField,
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
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
                text = when (validatedMillilitersCount.reason) {
                    is IncorrectMillilitersInput.Reason.Empty -> {
                        stringResource(R.string.waterTracking_UpdateWaterTrack_millilitersEmpty_text)
                    }

                    is IncorrectMillilitersInput.Reason.MoreThanDailyWaterIntake -> {
                        stringResource(R.string.waterTracking_UpdateWaterTrack_millilitersMoreThanDailyWaterIntake_text)
                    }

                    else -> {
                        stringResource(id = 0)
                    }
                }
            )
        }
    }
}

@Composable
private fun SelectDrinkTypeSection(
    state: WaterTrackUpdateViewModel.State,
    updateSelectedDrink: (DrinkType) -> Unit,
    onManageDrinkType: () -> Unit
) {
    Title(text = stringResource(id = R.string.waterTracking_UpdateWaterTrack_enterDrinkType_text))
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        maxItemsInEachRow = 8
    ) {
        DrinkTypeItem(
            modifier = Modifier.padding(4.dp),
            interactionType = InteractionType.ACTION,
            selectedDrinkType = state.selectedDrink,
            drinkType = DrinkType(
                id = 0,
                name = stringResource(id = R.string.waterTracking_CreateWaterTrack_createDrinkType_management_text),
                icon = LocalIconImpl(0, R.drawable.ic_create),
                hydrationIndexPercentage = 0
            ),
            onSelect = {
                onManageDrinkType()
            }
        )
        state.drinks.forEach { drink ->
            DrinkTypeItem(
                modifier = Modifier.padding(4.dp),
                drinkType = drink,
                selectedDrinkType = state.selectedDrink,
                onSelect = updateSelectedDrink
            )
        }
    }
}

@Composable
private fun SelectDateSection(
    state: WaterTrackUpdateViewModel.State,
    onShowDatePicker: () -> Unit
) {
    val selectedDate = state.selectedDate.date.toDate()
    val formattedDate = DateFormat.getDateInstance().format(selectedDate)

    Title(text = stringResource(id = R.string.waterTracking_UpdateWaterTrack_selectAnotherDate_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(
        text = stringResource(
            id = R.string.waterTracking_UpdateWaterTrack_selectedDate_formatText,
            formatArgs = arrayOf(formattedDate)
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    IconTextButton(
        iconResId = R.drawable.ic_date_range,
        labelResId = R.string.waterTracking_UpdateWaterTrack_selectDateRange_buttonText,
        style = ButtonStyles.OUTLINED,
        onClick = onShowDatePicker
    )
}