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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hardcoder.dev.healther.R
import hardcoder.dev.healther.data.local.room.entities.DrinkType
import hardcoder.dev.healther.ui.base.LocalPresentationModule
import hardcoder.dev.healther.ui.base.composables.IconTextButton
import hardcoder.dev.healther.ui.base.composables.ScaffoldWrapper

@Composable
fun SaveWaterTrackScreen(onGoBack: () -> Unit, onSaved: () -> Unit) {
    ScaffoldWrapper(
        titleResId = R.string.create_water_track_title,
        content = { SaveWaterTrackContent(onSaved = onSaved) },
        onGoBack = onGoBack
    )
}

@Composable
fun SaveWaterTrackContent(onSaved: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = presentationModule.createSaveWaterTrackViewModel()
    val state = viewModel.state.collectAsState()

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.enter_milliliters_count_label),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = state.value.millilitersCount.toString(),
            onValueChange = {
                if (it.isNotBlank()) {
                    Log.d("dddw", it.toInt().toString())
                    viewModel.updateWaterDrunk(it.toInt())
                }
            },
            label = {
                Text(
                    text = stringResource(id = R.string.enter_milliliters_count_hint),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.enter_drink_type_hint),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        val items = listOf(
            Drink(type = DrinkType.WATER, image = R.drawable.water),
            Drink(type = DrinkType.TEA, image = R.drawable.tea),
            Drink(type = DrinkType.COFFEE, image = R.drawable.coffee),
            Drink(type = DrinkType.BEER, image = R.drawable.beer),
            Drink(type = DrinkType.JUICE, image = R.drawable.juice),
            Drink(type = DrinkType.SODA, image = R.drawable.soda),
            Drink(type = DrinkType.SOUP, image = R.drawable.soup),
            Drink(type = DrinkType.MILK, image = R.drawable.milk)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { drink ->
                DrinkItem(
                    drink = drink,
                    onSelected = {
                        viewModel.updateDrinkType(drink.type)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconTextButton(
            iconResourceId = Icons.Default.Done,
            labelResId = R.string.save_label,
            onClick = {
                viewModel.createWaterTrack()
                onSaved()
            }
        )
    }
}


data class Drink(
    val type: DrinkType,
    @DrawableRes val image: Int
)

@Composable
fun DrinkItem(drink: Drink, onSelected: (Drink) -> Unit) {
    var isSelected by remember {
        mutableStateOf(false)
    }

    val selectedBorder = if (isSelected) BorderStroke(
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
            isSelected = true
            onSelected(drink)
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

@Preview
@Composable
fun DrinkItemPreview() {
    val drink = Drink(type = DrinkType.TEA, image = R.drawable.tea)
    DrinkItem(drink = drink, onSelected = {})
}

@Preview
@Composable
fun SaveWaterTrackScreenPreview() {
    SaveWaterTrackScreen(onGoBack = {}, onSaved = {})
}