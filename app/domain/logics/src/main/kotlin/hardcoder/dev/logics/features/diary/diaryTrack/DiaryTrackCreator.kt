package hardcoder.dev.logic.features.diary.diaryTrack

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.entities.features.diary.DiaryAttachmentGroup
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentManager
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class DiaryTrackCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val diaryAttachmentManager: DiaryAttachmentManager,
) {

    suspend fun create(
        diaryAttachmentGroup: DiaryAttachmentGroup,
        date: Instant,
        content: String,
    ) = withContext(dispatchers.io) {
        val diaryTrackId = idGenerator.nextId()
        appDatabase.diaryTrackQueries.insert(
            id = idGenerator.nextId(),
            content = content,
            date = date,
        )

        diaryAttachmentManager.attach(
            diaryTrackId = diaryTrackId,
            attachmentGroup = diaryAttachmentGroup,
        )
    }
}