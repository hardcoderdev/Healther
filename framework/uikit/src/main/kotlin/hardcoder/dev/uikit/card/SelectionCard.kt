@file:OptIn(ExperimentalMaterial3Api::class)

package hardcoder.dev.uikit.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun <T : Any> SelectionCard(
    modifier: Modifier,
    selectedItem: T?,
    item: T,
    onSelect: (T) -> Unit,
    cardContent: @Composable () -> Unit
) {
    val selectedBorder = if (item == selectedItem) BorderStroke(
        width = 3.dp,
        color = MaterialTheme.colorScheme.primary
    ) else null

    Card(
        onClick = { onSelect(item) },
        border = selectedBorder,
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 4.dp)
    ) {
        cardContent()
    }
}