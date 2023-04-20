package hardcoder.dev.logic.features.moodTracking.moodTrack

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.dashboard.features.diary.AttachmentType
import hardcoder.dev.logic.dashboard.features.diary.AttachmentTypeIdMapper
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentGroup
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentProvider
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackUpdater
import hardcoder.dev.logic.features.moodTracking.activity.Activity
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivityCreator
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivityDeleter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class MoodTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val diaryAttachmentProvider: DiaryAttachmentProvider,
    private val diaryTrackProvider: DiaryTrackProvider,
    private val diaryTrackUpdater: DiaryTrackUpdater,
    private val moodWithActivityDeleter: MoodWithActivityDeleter,
    private val moodWithActivityCreator: MoodWithActivityCreator,
    private val attachmentTypeIdMapper: AttachmentTypeIdMapper
) {

    suspend fun update(
        note: String?,
        moodTrack: MoodTrack,
        selectedActivities: List<Activity>
    ) {
        withContext(dispatcher) {
            appDatabase.moodTrackQueries.update(
                id = moodTrack.id,
                moodTypeId = moodTrack.moodType.id,
                date = moodTrack.date
            )
        }

        appDatabase.diaryAttachmentQueries.provideAttachmentByEntityId(
            targetTypeId = attachmentTypeIdMapper.mapToId(AttachmentType.MOOD_TRACKING_ENTITY),
            targetId = moodTrack.id
        ).executeAsOneOrNull()?.let { attachmentDatabase ->
            diaryTrackProvider.provideDiaryTrackById(attachmentDatabase.diaryTrackId).first()
                ?.let { diaryTrack ->
                    diaryAttachmentProvider.provideAttachmentOfDiaryTrackById(diaryTrack.id)
                        .first()?.moodTracks

                    withContext(dispatcher) {
                        diaryTrackUpdater.update(
                            diaryTrack = note?.let {
                                diaryTrack.copy(content = note)
                            } ?: diaryTrack,
                            diaryAttachmentGroup = DiaryAttachmentGroup(
                                moodTracks = listOf(moodTrack)
                            )
                        )
                    }
                }
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