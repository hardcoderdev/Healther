package hardcoder.dev.logic.features.diary.diaryTrack

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.entities.features.diary.DiaryAttachmentGroup
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentManager
import kotlinx.coroutines.withContext

class DiaryTrackUpdater(
    private val appDatabase: AppDatabase,
    private val diaryAttachmentManager: DiaryAttachmentManager,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        id: Int,
        content: CorrectDiaryTrackContent,
        diaryAttachmentGroup: DiaryAttachmentGroup,
    ) = withContext(dispatchers.io) {
        appDatabase.diaryTrackQueries.update(
            id = id,
            content = content.data,
        )

        diaryAttachmentManager.attach(
            diaryTrackId = id,
            attachmentGroup = diaryAttachmentGroup,
        )
    }
}