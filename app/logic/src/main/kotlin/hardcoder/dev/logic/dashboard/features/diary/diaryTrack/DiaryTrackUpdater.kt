package hardcoder.dev.logic.dashboard.features.diary.diaryTrack

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentGroup
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiaryTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val diaryAttachmentManager: DiaryAttachmentManager
) {

    suspend fun update(
        diaryTrack: DiaryTrack,
        diaryAttachmentGroup: DiaryAttachmentGroup
    ) = withContext(dispatcher) {
        appDatabase.diaryTrackQueries.update(
            id = diaryTrack.id,
            content = diaryTrack.content,
        )

        diaryAttachmentManager.attach(
            diaryTrackId = diaryTrack.id,
            attachmentGroup = diaryAttachmentGroup
        )
    }
}