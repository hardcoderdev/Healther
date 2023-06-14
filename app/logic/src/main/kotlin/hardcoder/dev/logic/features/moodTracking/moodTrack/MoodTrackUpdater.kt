package hardcoder.dev.logic.features.moodTracking.moodTrack

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.features.diary.AttachmentType
import hardcoder.dev.logic.features.diary.AttachmentTypeIdMapper
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentGroup
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackCreator
import hardcoder.dev.logic.features.moodTracking.activity.Activity
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivityCreator
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivityDeleter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MoodTrackUpdater(
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher,
    private val moodWithActivityDeleter: MoodWithActivityDeleter,
    private val moodWithActivityCreator: MoodWithActivityCreator,
    private val attachmentTypeIdMapper: AttachmentTypeIdMapper,
    private val diaryTrackCreator: DiaryTrackCreator
) {

    suspend fun update(
        note: String?,
        moodTrack: MoodTrack,
        selectedActivities: Set<Activity>
    ) = withContext(ioDispatcher) {
        appDatabase.moodTrackQueries.update(
            id = moodTrack.id,
            moodTypeId = moodTrack.moodType.id,
            date = moodTrack.date
        )

        appDatabase.diaryAttachmentQueries.selectByTarget(
            targetTypeId = attachmentTypeIdMapper.mapToId(AttachmentType.MOOD_TRACKING_ENTITY),
            targetId = moodTrack.id
        ).executeAsList().forEach {
            appDatabase.diaryTrackQueries.delete(it.diaryTrackId)
        }

        appDatabase.diaryAttachmentQueries.deleteByTarget(
            targetTypeId = attachmentTypeIdMapper.mapToId(AttachmentType.MOOD_TRACKING_ENTITY),
            targetId = moodTrack.id
        )

        if (note != null) {
            diaryTrackCreator.create(
                diaryAttachmentGroup = DiaryAttachmentGroup(
                    moodTracks = listOf(moodTrack)
                ),
                content = note,
                date = moodTrack.date.toLocalDateTime(TimeZone.currentSystemDefault())
            )
        }

        moodWithActivityDeleter.deleteAllActivitiesByMoodTrackId(moodTrack.id)

        selectedActivities.forEach { activity ->
            moodWithActivityCreator.create(
                moodTrackId = moodTrack.id,
                activityId = activity.id
            )
        }
    }
}