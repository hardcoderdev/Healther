package hardcoder.dev.healther.ui.screens.waterTracking.update

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import hardcoder.dev.healther.R
import hardcoder.dev.healther.logic.validators.IncorrectMillilitersInput
import hardcoder.dev.healther.ui.base.LocalPresentationModule
import hardcoder.dev.healther.ui.base.composables.ButtonStyles
import hardcoder.dev.healther.ui.base.composables.Drink
import hardcoder.dev.healther.ui.base.composables.DrinkItem
import hardcoder.dev.healther.ui.base.composables.ErrorText
import hardcoder.dev.healther.ui.base.composables.FilledTextField
import hardcoder.dev.healther.ui.base.composables.IconTextButton
import hardcoder.dev.healther.ui.base.composables.ScaffoldWrapper
import hardcoder.dev.healther.ui.base.composables.TopBarConfig
import hardcoder.dev.healther.ui.base.composables.TopBarType
import hardcoder.dev.healther.ui.base.extensions.toDate
import hardcoder.dev.healther.ui.base.regex.RegexHolder
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.text.DateFormat

@Composable
fun UpdateWaterTrackScreen(
    waterTrackId: Int,
    onGoBack: () -> Unit,
    onSaved: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.createUpdateWaterTrackViewModel(waterTrackId)
    }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            UpdateWaterTrackContent(
                state = state.value,
                updateWaterDrunk = viewModel::updateWaterDrunk,
                updateSelectedDate = viewModel::updateSelectedDate,
                updateSelectedDrink = viewModel::updateSelectedDrink,
                updateWaterTrack = {
                    viewModel.updateWaterTrack()
                    onSaved()
                }
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.updateWaterTrack_update_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun UpdateWaterTrackContent(
    updateWaterTrack: () -> Unit,
    updateSelectedDrink: (Drink) -> Unit,
    updateSelectedDate: (LocalDate) -> Unit,
    updateWaterDrunk: (Int) -> Unit,
    state: UpdateWaterTrackViewModel.State
) {
    val dateDialogState = rememberMaterialDialogState()
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = state.selectedDrink) {
        listState.animateScrollToItem(state.drinks.indexOf(state.selectedDrink))
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Column(Modifier.weight(2f)) {
            EnterDrunkMillilitersSection(
                updateWaterDrunk = updateWaterDrunk,
                state = state
            )
            Spacer(modifier = Modifier.height(16.dp))
            SelectDrinkTypeSection(
                state = state,
                updateSelectedDrink = updateSelectedDrink
            )
            Spacer(modifier = Modifier.height(16.dp))
            SelectDateSection(dateDialogState = dateDialogState, state = state)
        }
        IconTextButton(
            iconResourceId = Icons.Default.Done,
            labelResId = R.string.updateWaterTrack_updateEntry_button,
            onClick = updateWaterTrack,
            isEnabled = state.creationAllowed
        )
        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton(text = stringResource(R.string.updateWaterTrack_selectDate_datePickerDialogButton))
                negativeButton(text = stringResource(R.string.updateWaterTrack_cancel_datePickerDialogButton))
            }
        ) {
            datepicker(
                title = stringResource(R.string.updateWaterTrack_selectDateTitle_datePicker),
                colors = DatePickerDefaults.colors(
                    headerBackgroundColor = MaterialTheme.colorScheme.primary,
                    headerTextColor = MaterialTheme.colorScheme.onPrimary,
                    dateActiveBackgroundColor = MaterialTheme.colorScheme.secondary
                ),
                allowedDateValidator = { selectedDate ->
                    selectedDate.toEpochDay() <= LocalDate.now().toEpochDays()
                }
            ) { selectedDate ->
                updateSelectedDate(selectedDate.toKotlinLocalDate())
            }
        }
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
                        stringResource(R.string.updateWaterTrack_emptyTextField_error)
                    }

                    is IncorrectMillilitersInput.Reason.LessThanMinimum -> {
                        stringResource(R.string.updateWaterTrack_millilitersLessThanMinimum_error)
                    }

                    is IncorrectMillilitersInput.Reason.MoreThanDailyWaterIntake -> {
                        stringResource(R.string.updateWaterTrack_millilitersMoreThanDailyWaterIntake_error)
                    }
                }
            )
        }
    }
}

@Composable
private fun SelectDrinkTypeSection(
    state: UpdateWaterTrackViewModel.State,
    updateSelectedDrink: (Drink) -> Unit
) {
    Text(
        text = stringResource(id = R.string.updateWaterTrack_enterDrinkType_text),
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(16.dp))
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(state.drinks) { drink ->
            DrinkItem(
                drink = drink,
                selectedDrink = state.selectedDrink,
                onUpdateSelectedDrink = updateSelectedDrink
            )
        }
    }
}

@Composable
private fun SelectDateSection(
    state: UpdateWaterTrackViewModel.State,
    dateDialogState: MaterialDialogState
) {
    val selectedDate = requireNotNull(state.selectedDate.toDate())
    val formattedDate = DateFormat.getDateInstance().format(selectedDate)

    Text(
        text = stringResource(id = R.string.updateWaterTrack_selectAnotherDate_text),
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(16.dp))
    IconTextButton(
        iconResourceId = Icons.Rounded.DateRange,
        labelResId = R.string.updateWaterTrack_selectDateRange_button,
        style = ButtonStyles.OUTLINED,
        onClick = dateDialogState::show
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(
            id = R.string.updateWaterTrack_selectedDateFormat_text,
            formattedDate
        ),
        style = MaterialTheme.typography.titleLarge
    )
}