package hardcoder.dev.uikit.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionCard(
    modifier: Modifier = Modifier,
    onSelect: () -> Unit,
    isSelected: Boolean,
    cardContent: @Composable () -> Unit
) {
    val selectedBorder = if (isSelected) {
        BorderStroke(
            width = 3.dp,
            color = MaterialTheme.colorScheme.primary
        )
    } else {
        BorderStroke(
            width = 0.dp,
            color = Color.Transparent
        )
    }

    androidx.compose.material3.Card(
        onClick = onSelect,
        modifier = modifier.border(selectedBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        cardContent()
    }
}