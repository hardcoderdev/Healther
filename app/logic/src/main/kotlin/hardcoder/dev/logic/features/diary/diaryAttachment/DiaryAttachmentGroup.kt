package hardcoder.dev.logic.features.diary.diaryAttachment

import hardcoder.dev.logic.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrack

data class DiaryAttachmentGroup(
    val fastingTracks: List<FastingTrack> = emptyList(),
    val moodTracks: List<MoodTrack> = emptyList(),
    val tags: List<DiaryTag> = emptyList()
)

