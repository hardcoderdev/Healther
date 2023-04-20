package hardcoder.dev.logic.dashboard.features.diary.diaryTrack

import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentGroup
import kotlinx.datetime.Instant

data class DiaryTrack(
    val id: Int,
    val content: String,
    val date: Instant,
    val diaryAttachmentGroup: DiaryAttachmentGroup?
)
