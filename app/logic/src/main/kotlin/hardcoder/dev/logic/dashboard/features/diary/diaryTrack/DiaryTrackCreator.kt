package hardcoder.dev.logic.dashboard.features.diary.diaryTrack

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentManager
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
    private val diaryAttachmentManager: DiaryAttachmentManager
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

        diaryAttachmentManager.attach(
            diaryTrackId = diaryTrackId,
            attachmentGroup = diaryAttachmentGroup
        )
    }
}