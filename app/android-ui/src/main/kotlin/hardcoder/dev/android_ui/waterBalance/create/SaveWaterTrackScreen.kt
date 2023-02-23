package hardcoder.dev.android_ui.waterBalance.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
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
import hardcoder.dev.android_ui.LocalPresentationModule
import hardcoder.dev.android_ui.RegexHolder
import hardcoder.dev.android_ui.waterBalance.DrinkItem
import hardcoder.dev.entities.waterTracking.DrinkType
import hardcoder.dev.extensions.toDate
import hardcoder.dev.healther.R
import hardcoder.dev.logic.waterBalance.validators.IncorrectMillilitersInput
import hardcoder.dev.presentation.waterBalance.SaveWaterTrackViewModel
import hardcoder.dev.uikit.ButtonStyles
import hardcoder.dev.uikit.ErrorText
import hardcoder.dev.uikit.FilledTextField
import hardcoder.dev.uikit.IconTextButton
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.text.DateFormat

@Composable
fun SaveWaterTrackScreen(
    onGoBack: () -> Unit,
    onSaved: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.createSaveWaterTrackViewModel()
    }
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.value.creationState) {
        if (state.value.creationState is SaveWaterTrackViewModel.CreationState.Executed) {
            onSaved()
        }
    }

    ScaffoldWrapper(
        content = {
            SaveWaterTrackContent(
                state = state.value,
                updateWaterDrunk = viewModel::updateWaterDrunk,
                updateSelectedDate = viewModel::updateSelectedDate,
                updateSelectedDrink = viewModel::updateSelectedDrink,
                createWaterTrack = viewModel::createWaterTrack
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.saveWaterTrack_create_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun SaveWaterTrackContent(
    createWaterTrack: () -> Unit,
    updateSelectedDrink: (DrinkType) -> Unit,
    updateSelectedDate: (LocalDate) -> Unit,
    updateWaterDrunk: (Int) -> Unit,
    state: SaveWaterTrackViewModel.State
) {
    val dateDialogState = rememberMaterialDialogState()

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
            labelResId = R.string.saveWaterTrack_saveEntry_button,
            onClick = createWaterTrack,
            isEnabled = state.creationAllowed
        )
        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton(text = stringResource(R.string.saveWaterTrack_selectDate_datePickerDialogButton))
                negativeButton(text = stringResource(R.string.saveWaterTrack_cancel_datePickerDialogButton))
            }
        ) {
            datepicker(
                title = stringResource(R.string.saveWaterTrack_selectDateTitle_datePicker),
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
    state: SaveWaterTrackViewModel.State,
    updateWaterDrunk: (Int) -> Unit,
) {
    val validatedMillilitersCount = state.validatedMillilitersCount
    val validatedByRegexMillilitersCount = validatedMillilitersCount?.data?.value ?: 0

    Text(
        text = stringResource(id = R.string.saveWaterTrack_enterMillilitersCount_text),
        style = MaterialTheme.typography.titleLarge
    )
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
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = when (validatedMillilitersCount.reason) {
                    is IncorrectMillilitersInput.Reason.Empty -> {
                        stringResource(R.string.saveWaterTrack_millilitersEmpty_error)
                    }

                    is IncorrectMillilitersInput.Reason.MoreThanDailyWaterIntake -> {
                        stringResource(R.string.saveWaterTrack_millilitersMoreThanDailyWaterIntake_error)
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
    state: SaveWaterTrackViewModel.State,
    updateSelectedDrink: (DrinkType) -> Unit
) {
    Text(
        text = stringResource(id = R.string.saveWaterTrack_enterDrinkType_text),
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(16.dp))
    ScrollableTabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = state.drinks.indexOf(state.selectedDrink),
        indicator = {},
        divider = {},
        edgePadding = 0.dp
    ) {
        state.drinks.forEach { drink ->
            DrinkItem(
                modifier = Modifier.padding(12.dp),
                drinkType = drink,
                selectedDrink = state.selectedDrink,
                onUpdateSelectedDrink = {
                    updateSelectedDrink(it.drinkType)
                }
            )
        }
    }
}

@Composable
private fun SelectDateSection(
    state: SaveWaterTrackViewModel.State,
    dateDialogState: MaterialDialogState
) {
    val selectedDate = state.selectedDate.toDate()
    val formattedDate = DateFormat.getDateInstance().format(selectedDate)

    Text(
        text = stringResource(id = R.string.saveWaterTrack_selectAnotherDate_text),
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(16.dp))
    IconTextButton(
        iconResourceId = Icons.Rounded.DateRange,
        labelResId = R.string.saveWaterTrack_selectDateRange_button,
        style = ButtonStyles.OUTLINED,
        onClick = dateDialogState::show
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(id = R.string.saveWaterTrack_selectedDateFormat_text, formattedDate),
        style = MaterialTheme.typography.titleLarge
    )
}