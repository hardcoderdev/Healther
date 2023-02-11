@file:OptIn(ExperimentalMaterial3Api::class)

package hardcoder.dev.healther.ui.screens.waterTracking.create

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import hardcoder.dev.healther.R
import hardcoder.dev.healther.data.local.room.entities.DrinkType
import hardcoder.dev.healther.ui.base.LocalPresentationModule
import hardcoder.dev.healther.ui.base.composables.IconTextButton
import hardcoder.dev.healther.ui.base.composables.ScaffoldWrapper

@Composable
fun SaveWaterTrackScreen(
    waterTrackId: Int,
    onGoBack: () -> Unit,
    onSaved: () -> Unit
) {
    ScaffoldWrapper(
        titleResId = if (waterTrackId != -1) R.string.update_water_track_title else R.string.create_water_track_title,
        content = {
            SaveWaterTrackContent(
                onSaved = onSaved,
                waterTrackId = waterTrackId
            )
        },
        onGoBack = onGoBack
    )
}

@Composable
fun SaveWaterTrackContent(onSaved: () -> Unit, waterTrackId: Int) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.createSaveWaterTrackViewModel()
    }
    val state = viewModel.state.collectAsState()
    val millilitersCount = state.value.millilitersCount
    val countRegex = "[0-9]{0,9}$".toRegex()

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    Log.d("CHEECK", waterTrackId.toString())

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.enter_milliliters_count_label),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = if (millilitersCount == 0) "" else millilitersCount.toString(),
            onValueChange = {
                if (countRegex.matches(it)) {
                    try {
                        viewModel.updateWaterDrunk(it.toInt())
                    } catch (e: NumberFormatException) {
                        viewModel.updateWaterDrunk(0)
                    }
                }
            },
            label = {
                Text(
                    text = stringResource(id = R.string.enter_milliliters_count_hint),
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
            text = stringResource(id = R.string.enter_drink_type_hint),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.value.drinks) { drink ->
                DrinkItem(drink = drink)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.select_date_time_hint),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        IconTextButton(
            iconResourceId = Icons.Rounded.DateRange,
            labelResId = R.string.select_day_label,
            onClick = dateDialogState::show
        )
        Spacer(modifier = Modifier.height(32.dp))
        IconTextButton(
            iconResourceId = Icons.Default.Done,
            labelResId = R.string.save_label,
            onClick = {
                if (waterTrackId != -1) {
                    viewModel.updateWaterTrack()
                } else {
                    viewModel.createWaterTrack()
                }
                onSaved()
            }
        )
        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton(text = stringResource(R.string.select_option)) {
                    timeDialogState.show()
                }
                negativeButton(text = stringResource(R.string.cancel_option))
            }
        ) {
            datepicker(
                title = stringResource(R.string.select_water_track_date),
                colors = DatePickerDefaults.colors(
                    headerBackgroundColor = MaterialTheme.colorScheme.primary,
                    headerTextColor = MaterialTheme.colorScheme.onPrimary,
                    dateActiveBackgroundColor = MaterialTheme.colorScheme.secondary
                )
            ) { selectedDate ->
                viewModel.updateSelectedDate(selectedDate)
            }
        }
    }
}


data class Drink(
    val type: DrinkType,
    @DrawableRes val image: Int
)

@Composable
fun DrinkItem(drink: Drink) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.createSaveWaterTrackViewModel()
    }
    val state = viewModel.state.collectAsState()

    val selectedBorder = if (state.value.selectedDrink == drink) BorderStroke(
        width = 3.dp,
        color = MaterialTheme.colorScheme.primary
    ) else null

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 16.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = selectedBorder,
        onClick = {
            viewModel.updateSelectedDrink(drink)
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = drink.type.transcriptionResId),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = drink.image),
                contentDescription = stringResource(id = drink.type.transcriptionResId),
                modifier = Modifier.size(120.dp)
            )
        }
    }
}