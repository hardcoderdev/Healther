@file:OptIn(ExperimentalMaterial3Api::class)

package hardcoder.dev.healther.ui.screens.waterTracking.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import hardcoder.dev.healther.R
import hardcoder.dev.healther.ui.base.LocalPresentationModule
import hardcoder.dev.healther.ui.base.composables.Drink
import hardcoder.dev.healther.ui.base.composables.DrinkItem
import hardcoder.dev.healther.ui.base.composables.IconTextButton
import hardcoder.dev.healther.ui.base.composables.ScaffoldWrapper
import hardcoder.dev.healther.ui.base.composables.TopBarConfig
import hardcoder.dev.healther.ui.base.composables.TopBarType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

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

    ScaffoldWrapper(
        content = {
            SaveWaterTrackContent(
                state = state,
                updateWaterDrunk = viewModel::updateWaterDrunk,
                updateSelectedDate = viewModel::updateSelectedDate,
                updateSelectedDrink = viewModel::updateSelectedDrink,
                createWaterTrack = {
                    viewModel.createWaterTrack()
                    onSaved()
                }
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
    updateSelectedDrink: (Drink) -> Unit,
    updateSelectedDate: (LocalDate) -> Unit,
    updateWaterDrunk: (Int) -> Unit,
    state: State<SaveWaterTrackViewModel.State>
) {
    val countRegex = "[0-9]{0,9}$".toRegex()
    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()
    val millilitersCount = state.value.millilitersCount

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.saveWaterTrack_enterMillilitersCount_text),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = if (millilitersCount == 0) "" else millilitersCount.toString(),
            onValueChange = {
                if (countRegex.matches(it)) {
                    try {
                        updateWaterDrunk(it.toInt())
                    } catch (e: NumberFormatException) {
                        updateWaterDrunk(0)
                    }
                }
            },
            label = {
                Text(
                    text = stringResource(id = R.string.saveWaterTrack_enterMillilitersCountHint_textField),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.saveWaterTrack_enterDrinkType_text),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.value.drinks) { drink ->
                DrinkItem(
                    drink = drink,
                    selectedDrink = state.value.selectedDrink,
                    onUpdateSelectedDrink = updateSelectedDrink
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.saveWaterTrack_selectAnotherDate_text),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        IconTextButton(
            iconResourceId = Icons.Rounded.DateRange,
            labelResId = R.string.saveWaterTrack_selectDateRange_button,
            onClick = dateDialogState::show
        )
        Spacer(modifier = Modifier.height(32.dp))
        IconTextButton(
            iconResourceId = Icons.Default.Done,
            labelResId = R.string.saveWaterTrack_saveEntry_button,
            onClick = createWaterTrack
        )
        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton(text = stringResource(R.string.saveWaterTrack_selectDate_datePickerDialogButton)) {
                    timeDialogState.show()
                }
                negativeButton(text = stringResource(R.string.saveWaterTrack_cancel_datePickerDialogButton))
            }
        ) {
            datepicker(
                title = stringResource(R.string.saveWaterTrack_selectDateTitle_datePicker),
                colors = DatePickerDefaults.colors(
                    headerBackgroundColor = MaterialTheme.colorScheme.primary,
                    headerTextColor = MaterialTheme.colorScheme.onPrimary,
                    dateActiveBackgroundColor = MaterialTheme.colorScheme.secondary
                )
            ) { selectedDate ->
                updateSelectedDate(selectedDate.toKotlinLocalDate())
            }
        }
    }
}