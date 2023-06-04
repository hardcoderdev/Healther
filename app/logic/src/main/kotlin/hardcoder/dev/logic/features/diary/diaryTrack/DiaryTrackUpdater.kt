package hardcoder.dev.logic.features.diary.diaryTrack

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentGroup
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiaryTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val diaryAttachmentManager: DiaryAttachmentManager
) {

    suspend fun update(
        id: Int,
        content: CorrectDiaryTrackContent,
        diaryAttachmentGroup: DiaryAttachmentGroup
    ) = withContext(dispatcher) {
        appDatabase.diaryTrackQueries.update(
            id = id,
            content = content.data,
        )

        diaryAttachmentManager.attach(
            diaryTrackId = id,
            attachmentGroup = diaryAttachmentGroup
        )
    }
}