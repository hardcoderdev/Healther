package hardcoder.dev.logic.features.diary.diaryTrack

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentGroup
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
    ): Int {
        val diaryTrackId = idGenerator.nextId()

        withContext(dispatchers.io) {
            appDatabase.diaryTrackQueries.insert(
                id = diaryTrackId,
                content = content,
                date = date,
            )

            diaryAttachmentManager.attach(
                diaryTrackId = diaryTrackId,
                attachmentGroup = diaryAttachmentGroup,
            )
        }

        return diaryTrackId
    }
}