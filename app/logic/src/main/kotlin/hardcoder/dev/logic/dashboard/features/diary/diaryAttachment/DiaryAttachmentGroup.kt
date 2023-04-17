package hardcoder.dev.logic.dashboard.features.diary.diaryAttachment

import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrack

data class DiaryAttachmentGroup(
    val diaryTrackId: Int,
    val fastingTracks: List<FastingTrack>,
    val moodTracks: List<MoodTrack>,
    val tags: List<DiaryTag>
)


