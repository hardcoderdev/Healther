package hardcoder.dev.androidApp.ui.icons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.logic.icons.LocalIcon
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.card.Card
import hardcoder.dev.uikit.icons.Icon

@Composable
fun IconItem(
    icon: LocalIcon,
    contentDescriptionResId: Int,
    selectedIcon: LocalIcon,
    onSelectIcon: (LocalIcon) -> Unit
) {
    Card(
        interactionType = InteractionType.SELECTION,
        onClick = { onSelectIcon(icon) },
        item = icon,
        selectedItem = selectedIcon,
        modifier = Modifier.size(60.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            Icon(
                iconResId = icon.resourceId,
                contentDescription = stringResource(id = contentDescriptionResId),
                modifier = Modifier
                    .size(60.dp)
                    .padding(12.dp)
            )
        }
    }
}