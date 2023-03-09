@file:OptIn(ExperimentalMaterial3Api::class)

package hardcoder.dev.android_app.ui.features.waterBalance

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hardcoder.dev.android_app.ui.LocalDrinkTypeResourcesProvider
import hardcoder.dev.entities.waterTracking.DrinkType

@Composable
fun DrinkItem(
    modifier: Modifier = Modifier,
    drinkType: DrinkType,
    selectedDrink: DrinkType,
    onUpdateSelectedDrink: (DrinkTypeResources) -> Unit
) {
    val drinkTypeResourcesProvider = LocalDrinkTypeResourcesProvider.current
    val drinkTypeResources = drinkTypeResourcesProvider.provide(drinkType)

    val selectedBorder = if (selectedDrink == drinkType) BorderStroke(
        width = 3.dp,
        color = MaterialTheme.colorScheme.primary
    ) else null

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = selectedBorder,
        onClick = { onUpdateSelectedDrink(drinkTypeResources) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = drinkTypeResources.title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = drinkTypeResources.image),
                contentDescription = stringResource(id = drinkTypeResources.title),
                modifier = Modifier.size(120.dp)
            )
        }
    }
}