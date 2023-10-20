package hardcoder.dev.logics.features.moodTracking.moodTrack

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.diary.DiaryAttachmentDao
import hardcoder.dev.database.dao.features.diary.DiaryTrackDao
import hardcoder.dev.database.dao.features.moodTracking.MoodTrackDao
import hardcoder.dev.database.entities.features.moodTracking.MoodTrack
import hardcoder.dev.entities.features.diary.AttachmentType
import hardcoder.dev.entities.features.diary.DiaryAttachmentGroup
import hardcoder.dev.entities.features.moodTracking.MoodActivity
import hardcoder.dev.logics.features.diary.diaryTrack.DiaryTrackCreator
import hardcoder.dev.logics.features.moodTracking.moodWithActivity.MoodWithActivityCreator
import hardcoder.dev.logics.features.moodTracking.moodWithActivity.MoodWithActivityDeleter
import hardcoder.dev.mappers.features.diary.AttachmentTypeIdMapper
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import hardcoder.dev.entities.features.moodTracking.MoodTrack as MoodTrackEntity

class MoodTrackUpdater(
    private val moodTrackDao: MoodTrackDao,
    private val diaryAttachmentDao: DiaryAttachmentDao,
    private val diaryTrackDao: DiaryTrackDao,
    private val moodWithActivityDeleter: MoodWithActivityDeleter,
    private val moodWithActivityCreator: MoodWithActivityCreator,
    private val attachmentTypeIdMapper: AttachmentTypeIdMapper,
    private val diaryTrackCreator: DiaryTrackCreator,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        note: String?,
        moodTrack: MoodTrackEntity,
        selectedActivities: Set<MoodActivity>,
    ) = withContext(dispatchers.io) {
        moodTrackDao.update(
            MoodTrack(
                id = moodTrack.id,
                moodTypeId = moodTrack.moodType.id,
                creationDate = moodTrack.creationDate,
            ),
        )

        diaryAttachmentDao.provideDiaryAttachmentsByTargetId(
            targetTypeId = attachmentTypeIdMapper.mapToId(AttachmentType.MOOD_TRACKING_ENTITY),
            targetId = moodTrack.id,
        ).onEach { diaryAttachmentList ->
            diaryAttachmentList.forEach {
                diaryTrackDao.deleteById(it.diaryTrackId)
            }
        }

        diaryAttachmentDao.deleteDiaryAttachmentsByTargetId(
            targetTypeId = attachmentTypeIdMapper.mapToId(AttachmentType.MOOD_TRACKING_ENTITY),
            targetId = moodTrack.id,
        )

        if (note != null) {
            diaryTrackCreator.create(
                content = note,
                date = moodTrack.creationDate,
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