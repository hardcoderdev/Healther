package hardcoder.dev.logic.features.diary.diaryTrack

import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentGroup
import kotlinx.datetime.Instant

data class DiaryTrack(
    val id: Int,
    val content: String,
    val date: Instant,
    val diaryAttachmentGroup: DiaryAttachmentGroup?,
    val isRewardCollected: Boolean,
)