package hardcoder.dev.androidApp.ui.screens.features.diary.items

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
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrack
import hardcoder.dev.uikit.components.card.Card
import hardcoder.dev.uikit.components.card.CardConfig
import hardcoder.dev.uikit.components.chip.Chip
import hardcoder.dev.uikit.components.chip.ChipConfig
import hardcoder.dev.uikit.components.text.Description
import hardcoderdev.healther.app.android.app.R

private const val MAX_TAGS_VISIBLE_IN_ITEM = 4

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DiaryItem(
    diaryTrack: DiaryTrack,
    onUpdate: (DiaryTrack) -> Unit,
) {
    Card(
        cardConfig = CardConfig.Action(
            onClick = { onUpdate(diaryTrack) },
            cardContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Description(text = diaryTrack.content)
                    diaryTrack.diaryAttachmentGroup?.tags?.let { tags ->
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            maxItemsInEachRow = 3,
                        ) {
                            tags.take(MAX_TAGS_VISIBLE_IN_ITEM).forEach { tag ->
                                Chip(
                                    chipConfig = ChipConfig.Static(
                                        modifier = Modifier.padding(
                                            top = 6.dp,
                                            bottom = 6.dp,
                                            end = 6.dp,
                                        ),
                                        text = tag.name,
                                        iconResId = tag.icon.resourceId,
                                        shape = RoundedCornerShape(16.dp),
                                        padding = PaddingValues(8.dp),
                                    ),
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
            },
        ),
    )
}

@Composable
private fun TagCounterChip(tagsLeftCount: Int) {
    Chip(
        chipConfig = ChipConfig.Static(
            shape = RoundedCornerShape(16.dp),
            padding = PaddingValues(8.dp),
            iconResId = R.drawable.ic_list,
            modifier = Modifier.padding(top = 6.dp, bottom = 6.dp, end = 6.dp),
            text = stringResource(
                id = R.string.diary_item_tags_left_count_format,
                formatArgs = arrayOf(tagsLeftCount),
            ),
        ),
    )
}