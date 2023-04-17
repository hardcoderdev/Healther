package hardcoder.dev.logic.dashboard.features.diary.diaryTrack

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.dashboard.features.diary.AttachmentType
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentCreator
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTag
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
        entityId: Int?,
        attachmentType: AttachmentType?,
        date: LocalDateTime,
        description: String,
        title: String?,
        selectedTags: List<DiaryTag>
    ) = withContext(dispatcher) {
        val diaryTrackId = idGenerator.nextId()
        appDatabase.diaryTrackQueries.insert(
            id = diaryTrackId,
            title = title,
            description = description,
            date = date.toInstant(TimeZone.currentSystemDefault()),
        )

        entityId?.let {
            diaryAttachmentCreator.create(
                attachmentId = entityId,
                attachmentType = requireNotNull(attachmentType),
                diaryTrackId = diaryTrackId
            )
        }

        selectedTags.forEach { tag ->
            diaryAttachmentCreator.create(
                attachmentId = tag.id,
                attachmentType = AttachmentType.TAG,
                diaryTrackId = diaryTrackId
            )
        }
    }
}