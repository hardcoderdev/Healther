package hardcoder.dev.entities.features.diary

import kotlinx.datetime.Instant

data class DiaryTrack(
    val id: Int,
    val content: String,
    val creationInstant: Instant,
    val diaryAttachmentGroup: DiaryAttachmentGroup?,
)