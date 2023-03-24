@file:OptIn(ExperimentalLayoutApi::class)

package hardcoder.dev.androidApp.ui.features.waterBalance.update

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
import hardcoder.dev.androidApp.ui.LocalPresentationModule
import hardcoder.dev.androidApp.ui.RegexHolder
import hardcoder.dev.androidApp.ui.features.DeleteTrackDialog
import hardcoder.dev.androidApp.ui.features.waterBalance.DrinkTypeItem
import hardcoder.dev.entities.features.waterTracking.DrinkType
import hardcoder.dev.extensions.toDate
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.waterBalance.IncorrectMillilitersInput
import hardcoder.dev.presentation.features.waterBalance.UpdateWaterTrackViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
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
import kotlinx.datetime.LocalDate
import java.text.DateFormat

@Composable
fun UpdateWaterTrackScreen(
    waterTrackId: Int,
    onGoBack: () -> Unit,
    onCreateDrinkType: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.createUpdateWaterTrackViewModel(waterTrackId)
    }
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.value.updateState) {
        if (state.value.updateState is UpdateWaterTrackViewModel.UpdateState.Executed) {
            onGoBack()
        }
    }

    LaunchedEffect(key1 = state.value.deleteState) {
        if (state.value.deleteState is UpdateWaterTrackViewModel.DeleteState.Executed) {
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
                onCreateDrinkType = onCreateDrinkType,
                onUpdateWaterTrack = viewModel::updateWaterTrack
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.updateWaterTrack_update_title_topBar,
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
    state: UpdateWaterTrackViewModel.State,
    onUpdateWaterTrack: () -> Unit,
    onUpdateSelectedDrink: (DrinkType) -> Unit,
    onUpdateSelectedDate: (LocalDate) -> Unit,
    onUpdateWaterDrunk: (Int) -> Unit,
    onCreateDrinkType: () -> Unit
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
                onCreateDrinkType = onCreateDrinkType
            )
            Spacer(modifier = Modifier.height(32.dp))
            SelectDateSection(state = state) {
                datePickerDialogVisibility = !datePickerDialogVisibility
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconTextButton(
            iconResId = R.drawable.ic_done,
            labelResId = R.string.updateWaterTrack_updateEntry_button,
            onClick = onUpdateWaterTrack,
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
    state: UpdateWaterTrackViewModel.State,
    updateWaterDrunk: (Int) -> Unit,
) {
    val validatedMillilitersCount = state.validatedMillilitersCount
    val validatedByRegexMillilitersCount = validatedMillilitersCount?.data?.value ?: 0

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
        label = R.string.saveWaterTrack_enterMillilitersCountHint_textField,
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
                        stringResource(R.string.updateWaterTrack_millilitersEmpty_error)
                    }

                    is IncorrectMillilitersInput.Reason.MoreThanDailyWaterIntake -> {
                        stringResource(R.string.updateWaterTrack_millilitersMoreThanDailyWaterIntake_error)
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
    state: UpdateWaterTrackViewModel.State,
    updateSelectedDrink: (DrinkType) -> Unit,
    onCreateDrinkType: () -> Unit
) {
    Title(text = stringResource(id = R.string.updateWaterTrack_enterDrinkType_text))
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        maxItemsInEachRow = 8
    ) {
        state.drinks.forEach { drink ->
            DrinkTypeItem(
                modifier = Modifier.padding(4.dp),
                drinkType = drink,
                selectedDrinkType = state.selectedDrink,
                onSelect = updateSelectedDrink
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    IconTextButton(
        iconResId = R.drawable.ic_create,
        labelResId = R.string.updateWaterTrack_createCustomDrinkType_buttonText,
        onClick = onCreateDrinkType
    )
}

@Composable
private fun SelectDateSection(
    state: UpdateWaterTrackViewModel.State,
    onShowDatePicker: () -> Unit
) {
    val selectedDate = state.selectedDate.toDate()
    val formattedDate = DateFormat.getDateInstance().format(selectedDate)

    Title(text = stringResource(id = R.string.updateWaterTrack_selectAnotherDate_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(
        text = stringResource(
            id = R.string.updateWaterTrack_selectedDateFormat_text,
            formatArgs = arrayOf(formattedDate)
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    IconTextButton(
        iconResId = R.drawable.ic_date_range,
        labelResId = R.string.updateWaterTrack_selectDateRange_button,
        style = ButtonStyles.OUTLINED,
        onClick = onShowDatePicker
    )
}