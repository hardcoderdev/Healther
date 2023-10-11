package hardcoder.dev.entities.features.diary

data class DiaryAttachment(
    val id: Int,
    val diaryTrackId: Int,
    val targetType: AttachmentType,
    val targetId: Int,
)