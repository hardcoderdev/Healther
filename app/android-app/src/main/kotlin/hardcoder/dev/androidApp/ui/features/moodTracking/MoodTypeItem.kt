package hardcoder.dev.androidApp.ui.features.moodTracking

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.card.Card
import hardcoder.dev.uikit.text.Label

@Composable
fun MoodItem(
    modifier: Modifier = Modifier,
    moodType: MoodType,
    selectedMoodType: MoodType?,
    onSelect: () -> Unit
) {
    Card(
        interactionType = InteractionType.SELECTION,
        item = moodType,
        selectedItem = selectedMoodType,
        modifier = modifier,
        onClick = onSelect
    ) {
        Column(
            modifier = Modifier
                .width(130.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.height(60.dp),
                painter = painterResource(id = moodType.icon.resourceId),
                contentDescription = moodType.name,
                alignment = Alignment.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Label(text = moodType.name)
        }
    }
}