package hardcoder.dev.presentation.features.moodTracking

import hardcoder.dev.entities.features.moodTracking.HobbyTrack
import hardcoder.dev.entities.features.moodTracking.MoodTrack

data class MoodTrackWithHobbies(
    val moodTrack: MoodTrack,
    val hobbyTrackList: List<HobbyTrack?>?
)