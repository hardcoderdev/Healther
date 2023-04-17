package hardcoder.dev.logic.features.moodTracking.moodTrack

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.dashboard.features.diary.AttachmentType
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackCreator
import hardcoder.dev.logic.features.moodTracking.activity.Activity
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivityCreator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class MoodTrackCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val diaryTrackCreator: DiaryTrackCreator,
    private val moodWithActivityCreator: MoodWithActivityCreator
) {

    suspend fun create(
        note: String?,
        moodType: MoodType,
        date: LocalDateTime,
        selectedActivities: List<Activity>
    ) = withContext(dispatcher) {
        val moodTrackId = idGenerator.nextId()

        appDatabase.moodTrackQueries.insert(
            id = moodTrackId,
            moodTypeId = moodType.id,
            date = date.toInstant(TimeZone.currentSystemDefault())
        )

        note?.let {
            diaryTrackCreator.create(
                entityId = moodTrackId,
                description = note,
                title = null,
                date = date,
                selectedTags = emptyList(),
                attachmentType = AttachmentType.MOOD_TRACKING_ENTITY
            )
        }

        selectedActivities.forEach { activity ->
            moodWithActivityCreator.create(
                moodTrackId = moodTrackId,
                activityId = activity.id
            )
        }
    }
}