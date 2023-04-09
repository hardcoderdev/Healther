@file:OptIn(ExperimentalLayoutApi::class)

package hardcoder.dev.androidApp.ui.dashboard.diary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrack
import hardcoder.dev.logic.dashboard.features.diary.diaryWithFeatureType.DiaryWithFeatureTags
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.card.Card
import hardcoder.dev.uikit.chip.Chip
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Title

const val FIRST_SENTENCE_LAST_SYMBOL = 40

@Composable
fun DiaryItem(
    diaryWithFeatureTags: DiaryWithFeatureTags,
    onUpdate: (DiaryTrack) -> Unit
) {
    val diaryTrack = diaryWithFeatureTags.diaryTrack

    val textLastSymbol = diaryTrack.text.length
    val title = diaryTrack.title ?: run {
        if (textLastSymbol > FIRST_SENTENCE_LAST_SYMBOL) {
            diaryTrack.text.substring(0, FIRST_SENTENCE_LAST_SYMBOL)
        } else {
            diaryTrack.text.substring(0, textLastSymbol - 1)
        }
    }

    Card<DiaryTrack>(
        interactionType = InteractionType.ACTION,
        onClick = { onUpdate(diaryTrack) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Title(text = title)
            Spacer(modifier = Modifier.height(16.dp))
            Description(text = diaryTrack.text)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                maxItemsInEachRow = 3
            ) {
                diaryWithFeatureTags.featureTags.forEach { featureTag ->
                    Chip(
                        modifier = Modifier.padding(top = 6.dp, bottom = 6.dp, end = 6.dp),
                        text = featureTag.name,
                        shape = RoundedCornerShape(16.dp),
                        padding = PaddingValues(8.dp),
                        isSelected = false,
                        interactionType = InteractionType.STATIC
                    )
                }
            }
        }
    }
}