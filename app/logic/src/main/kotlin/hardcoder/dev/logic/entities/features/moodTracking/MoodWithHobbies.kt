package hardcoder.dev.logic.entities.features.moodTracking

data class MoodWithHobbies(
    val moodTrack: MoodTrack,
    val hobbyList: List<Hobby>
)