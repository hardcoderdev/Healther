package hardcoder.dev.entities.features.diary

import kotlinx.datetime.Instant

data class DiaryTrack(
    val id: Int,
    val content: String,
    val date: Instant,
    val diaryAttachmentGroup: DiaryAttachmentGroup?,
)