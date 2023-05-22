package hardcoder.dev.logic.features.diary.diaryAttachment

import hardcoder.dev.logic.features.diary.AttachmentType

data class DiaryAttachment(
    val id: Int,
    val diaryTrackId: Int,
    val targetType: AttachmentType,
    val targetId: Int
)
