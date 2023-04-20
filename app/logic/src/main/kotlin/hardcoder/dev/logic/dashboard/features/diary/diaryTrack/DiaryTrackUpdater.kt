package hardcoder.dev.logic.dashboard.features.diary.diaryTrack

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.dashboard.features.diary.AttachmentType
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentCreator
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentDeleter
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentGroup
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiaryTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val diaryAttachmentCreator: DiaryAttachmentCreator,
    private val diaryAttachmentDeleter: DiaryAttachmentDeleter
) {

    suspend fun update(
        diaryTrack: DiaryTrack,
        diaryAttachmentGroup: DiaryAttachmentGroup
    ) = withContext(dispatcher) {
        appDatabase.diaryTrackQueries.update(
            id = diaryTrack.id,
            content = diaryTrack.content,
        )

        diaryAttachmentDeleter.deleteAllDiaryTrackAttachmentsById(
            diaryTrackId = diaryTrack.id,
            targetType = AttachmentType.TAG
        )

        diaryAttachmentGroup.tags.forEach { tag ->
            diaryAttachmentCreator.create(
                targetId = tag.id,
                targetType = AttachmentType.TAG,
                diaryTrackId = diaryTrack.id
            )
        }
    }
}