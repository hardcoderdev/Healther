package hardcoder.dev.logic.dashboard.features.diary.diaryTrack

import kotlinx.datetime.Instant

data class DiaryTrack(
    val id: Int,
    val text: String,
    val title: String?,
    val date: Instant
)
