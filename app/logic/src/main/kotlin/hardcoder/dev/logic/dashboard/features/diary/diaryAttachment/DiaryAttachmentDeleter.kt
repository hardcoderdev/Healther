package hardcoder.dev.logic.dashboard.features.diary.diaryAttachment

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.dashboard.features.diary.AttachmentType
import hardcoder.dev.logic.dashboard.features.diary.AttachmentTypeIdMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiaryAttachmentDeleter(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val attachmentTypeIdMapper: AttachmentTypeIdMapper
) {

    suspend fun deleteAllDiaryTrackAttachmentsById(targetType: AttachmentType, diaryTrackId: Int) =
        withContext(dispatcher) {
            appDatabase.diaryAttachmentQueries.deleteAllDiaryAttachmentTagsOfDiaryTrackById(
                targetTypeId = attachmentTypeIdMapper.mapToId(targetType),
                diaryTrackId = diaryTrackId
            )
        }
}