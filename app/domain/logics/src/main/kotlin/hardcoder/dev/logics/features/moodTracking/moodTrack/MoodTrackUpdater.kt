package hardcoder.dev.logic.features.moodTracking.moodTrack

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.entities.features.diary.AttachmentType
import hardcoder.dev.entities.features.diary.DiaryAttachmentGroup
import hardcoder.dev.entities.features.moodTracking.MoodActivity
import hardcoder.dev.entities.features.moodTracking.MoodTrack
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackCreator
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivityCreator
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivityDeleter
import hardcoder.dev.mappers.features.diary.AttachmentTypeIdMapper
import kotlinx.coroutines.withContext

class MoodTrackUpdater(
    private val appDatabase: AppDatabase,
    private val moodWithActivityDeleter: MoodWithActivityDeleter,
    private val moodWithActivityCreator: MoodWithActivityCreator,
    private val attachmentTypeIdMapper: AttachmentTypeIdMapper,
    private val diaryTrackCreator: DiaryTrackCreator,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        note: String?,
        moodTrack: MoodTrack,
        selectedActivities: Set<MoodActivity>,
    ) = withContext(dispatchers.io) {
        appDatabase.moodTrackQueries.update(
            id = moodTrack.id,
            moodTypeId = moodTrack.moodType.id,
            date = moodTrack.date,
        )

        appDatabase.diaryAttachmentQueries.selectByTarget(
            targetTypeId = attachmentTypeIdMapper.mapToId(AttachmentType.MOOD_TRACKING_ENTITY),
            targetId = moodTrack.id,
        ).executeAsList().forEach {
            appDatabase.diaryTrackQueries.delete(it.diaryTrackId)
        }

        appDatabase.diaryAttachmentQueries.deleteByTarget(
            targetTypeId = attachmentTypeIdMapper.mapToId(AttachmentType.MOOD_TRACKING_ENTITY),
            targetId = moodTrack.id,
        )

        if (note != null) {
            diaryTrackCreator.create(
                content = note,
                date = moodTrack.date,
                diaryAttachmentGroup = DiaryAttachmentGroup(
                    moodTracks = listOf(moodTrack),
                ),
            )
        }

        moodWithActivityDeleter.deleteAllActivitiesByMoodTrackId(moodTrack.id)

        selectedActivities.forEach { activity ->
            moodWithActivityCreator.create(
                moodTrackId = moodTrack.id,
                activityId = activity.id,
            )
        }
    }
}