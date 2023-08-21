package hardcoder.dev.logic.features.moodTracking.moodTrack

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentGroup
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackCreator
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivity
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivityCreator
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class MoodTrackCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val diaryTrackCreator: DiaryTrackCreator,
    private val moodTrackProvider: MoodTrackProvider,
    private val moodWithActivityCreator: MoodWithActivityCreator,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        note: String?,
        moodType: MoodType,
        date: Instant,
        selectedActivities: Set<MoodActivity>,
    ): Int {
        val moodTrackId = idGenerator.nextId()

        withContext(dispatchers.io) {
            appDatabase.moodTrackQueries.insert(
                id = moodTrackId,
                moodTypeId = moodType.id,
                date = date,
            )

            if (note != null) {
                diaryTrackCreator.create(
                    content = note,
                    date = date,
                    diaryAttachmentGroup = DiaryAttachmentGroup(
                        moodTracks = listOf(
                            moodTrackProvider.provideById(moodTrackId).filterNotNull().first(),
                        ),
                    ),
                )
            }

            selectedActivities.forEach { activity ->
                moodWithActivityCreator.create(
                    moodTrackId = moodTrackId,
                    activityId = activity.id,
                )
            }
        }

        return moodTrackId
    }
}