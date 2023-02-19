@file:OptIn(ExperimentalMaterial3Api::class)

package hardcoder.dev.healther.ui.base.composables

import androidx.annotation.DrawableRes
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
import hardcoder.dev.healther.data.local.room.entities.DrinkType

data class Drink(
    val type: DrinkType,
    @DrawableRes val image: Int
)

@Composable
fun DrinkItem(
    modifier: Modifier = Modifier,
    drink: Drink,
    selectedDrink: Drink,
    onUpdateSelectedDrink: (Drink) -> Unit
) {
    val selectedBorder = if (selectedDrink == drink) BorderStroke(
        width = 3.dp,
        color = MaterialTheme.colorScheme.primary
    ) else null

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 16.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = selectedBorder,
        onClick = { onUpdateSelectedDrink(drink) }
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