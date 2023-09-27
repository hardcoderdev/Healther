package hardcoder.dev.database.appAdapters

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import hardcoder.dev.database.MoodActivity
import hardcoder.dev.database.MoodTrack
import hardcoder.dev.database.MoodType
import hardcoder.dev.database.MoodWithActivity
import hardcoder.dev.database.columnAdapters.InstantAdapter

object MoodTrackingAdapters {

    fun createMoodTrackAdapter() = MoodTrack.Adapter(
        dateAdapter = InstantAdapter,
        idAdapter = IntColumnAdapter,
        moodTypeIdAdapter = IntColumnAdapter,
    )

    fun createMoodActivityAdapter() = MoodActivity.Adapter(
        iconIdAdapter = IntColumnAdapter,
        idAdapter = IntColumnAdapter,
    )

    fun createMoodTypeAdapter() = MoodType.Adapter(
        idAdapter = IntColumnAdapter,
        iconIdAdapter = IntColumnAdapter,
        positivePercentageAdapter = IntColumnAdapter,
    )

    fun createMoodWithActivityAdapter() = MoodWithActivity.Adapter(
        idAdapter = IntColumnAdapter,
        activityIdAdapter = IntColumnAdapter,
        moodTrackIdAdapter = IntColumnAdapter,
    )
}