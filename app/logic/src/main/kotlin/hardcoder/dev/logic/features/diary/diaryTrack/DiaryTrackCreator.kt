package hardcoder.dev.logic.features.diary.diaryTrack

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentManager
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentGroup
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class DiaryTrackCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher,
    private val diaryAttachmentManager: DiaryAttachmentManager
) {

    suspend fun create(
        diaryAttachmentGroup: DiaryAttachmentGroup,
        date: LocalDateTime,
        content: String
    ) = withContext(ioDispatcher) {
        val diaryTrackId = idGenerator.nextId()
        appDatabase.diaryTrackQueries.insert(
            id = diaryTrackId,
            content = content,
            date = date.toInstant(TimeZone.currentSystemDefault()),
        )

        diaryAttachmentManager.attach(
            diaryTrackId = diaryTrackId,
            attachmentGroup = diaryAttachmentGroup
        )
    }
}