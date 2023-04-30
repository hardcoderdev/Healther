@file:OptIn(ExperimentalLayoutApi::class)

package hardcoder.dev.androidApp.ui.dashboard.diary.items

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.healther.R
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrack
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.card.Card
import hardcoder.dev.uikit.chip.Chip
import hardcoder.dev.uikit.text.Description

private const val MAX_TAGS_VISIBLE_IN_ITEM = 4

@Composable
fun DiaryItem(
    diaryTrack: DiaryTrack,
    onUpdate: (DiaryTrack) -> Unit
) {
    Card<DiaryTrack>(
        interactionType = InteractionType.ACTION,
        onClick = { onUpdate(diaryTrack) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Description(text = diaryTrack.content)
            diaryTrack.diaryAttachmentGroup?.tags?.let { tags ->
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    maxItemsInEachRow = 3
                ) {
                    tags.take(MAX_TAGS_VISIBLE_IN_ITEM).forEach { tag ->
                        Chip(
                            modifier = Modifier.padding(top = 6.dp, bottom = 6.dp, end = 6.dp),
                            text = tag.name,
                            iconResId = tag.icon.resourceId,
                            shape = RoundedCornerShape(16.dp),
                            padding = PaddingValues(8.dp),
                            isSelected = false,
                            interactionType = InteractionType.STATIC
                        )
                    }
                    if (tags.size > MAX_TAGS_VISIBLE_IN_ITEM) {
                        TagCounterChip(tagsLeftCount = tags.size - MAX_TAGS_VISIBLE_IN_ITEM)
                    }
                }
            }
            diaryTrack.diaryAttachmentGroup?.fastingTracks?.forEach {
                Spacer(modifier = Modifier.height(8.dp))
                DiaryFastingItem(fastingTrack = it)
            }
            diaryTrack.diaryAttachmentGroup?.moodTracks?.forEach {
                Spacer(modifier = Modifier.height(8.dp))
                DiaryMoodItem(moodTrack = it)
            }
        }
    }
}

@Composable
private fun TagCounterChip(tagsLeftCount: Int) {
    Chip(
        interactionType = InteractionType.ACTION,
        shape = RoundedCornerShape(16.dp),
        padding = PaddingValues(8.dp),
        iconResId = R.drawable.ic_list,
        modifier = Modifier.padding(top = 6.dp, bottom = 6.dp, end = 6.dp),
        text = stringResource(
            id = R.string.diary_item_tags_left_count_format,
            formatArgs = arrayOf(tagsLeftCount)
        )
    )
}