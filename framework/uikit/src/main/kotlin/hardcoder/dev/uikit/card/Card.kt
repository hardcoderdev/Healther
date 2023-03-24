@file:OptIn(ExperimentalMaterial3Api::class)

package hardcoder.dev.uikit.card

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Card(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    cardContent: @Composable () -> Unit
) {
    androidx.compose.material3.Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        cardContent()
    }
}