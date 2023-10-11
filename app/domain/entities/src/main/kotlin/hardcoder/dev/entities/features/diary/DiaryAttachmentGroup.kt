package hardcoder.dev.entities.features.diary

import hardcoder.dev.entities.features.moodTracking.MoodTrack

data class DiaryAttachmentGroup(
    val moodTracks: List<MoodTrack> = emptyList(),
    val tags: Set<DiaryTag> = emptySet(),
)