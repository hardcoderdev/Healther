@file:OptIn(ExperimentalMaterial3Api::class)

package hardcoder.dev.androidApp.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun IconItem(
    iconResourceName: String,
    contentDescriptionResId: Int,
    selectedIconResourceName: String,
    onSelectIcon: (String) -> Unit
) {
    val iconResolver = LocalIconResolver.current

    val selectedBorder = if (iconResourceName == selectedIconResourceName) BorderStroke(
        width = 2.dp,
        color = MaterialTheme.colorScheme.onBackground
    ) else null

    Card(
        onClick = { onSelectIcon(iconResourceName) },
        border = selectedBorder,
        modifier = Modifier.size(60.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 8.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(id = iconResolver.toResourceId(iconResourceName)),
                contentDescription = stringResource(id = contentDescriptionResId),
                modifier = Modifier
                    .size(60.dp)
                    .padding(12.dp),
            )
        }
    }
}