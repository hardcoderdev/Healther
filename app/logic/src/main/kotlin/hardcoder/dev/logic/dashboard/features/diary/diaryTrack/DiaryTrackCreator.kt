package hardcoder.dev.logic.dashboard.features.diary.diaryTrack

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.dashboard.features.diary.AttachmentType
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentCreator
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentGroup
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class DiaryTrackCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val diaryAttachmentCreator: DiaryAttachmentCreator
) {

    suspend fun create(
        diaryAttachmentGroup: DiaryAttachmentGroup,
        date: LocalDateTime,
        content: String
    ) = withContext(dispatcher) {
        val diaryTrackId = idGenerator.nextId()
        appDatabase.diaryTrackQueries.insert(
            id = diaryTrackId,
            content = content,
            date = date.toInstant(TimeZone.currentSystemDefault()),
        )

        diaryAttachmentGroup.moodTracks.forEach { moodTrack ->
            diaryAttachmentCreator.create(
                targetId = moodTrack.id,
                targetType = requireNotNull(AttachmentType.MOOD_TRACKING_ENTITY),
                diaryTrackId = diaryTrackId
            )
        }

        diaryAttachmentGroup.fastingTracks.forEach { fastingTrack ->
            diaryAttachmentCreator.create(
                targetId = fastingTrack.id,
                targetType = requireNotNull(AttachmentType.FASTING_ENTITY),
                diaryTrackId = diaryTrackId
            )
        }

        diaryAttachmentGroup.tags.forEach { tag ->
            diaryAttachmentCreator.create(
                targetId = tag.id,
                targetType = AttachmentType.TAG,
                diaryTrackId = diaryTrackId
            )
        }
    }
}