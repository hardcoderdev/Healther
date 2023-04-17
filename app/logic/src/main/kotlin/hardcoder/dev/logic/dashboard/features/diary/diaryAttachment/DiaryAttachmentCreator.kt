package hardcoder.dev.logic.dashboard.features.diary.diaryAttachment

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.dashboard.features.diary.AttachmentType
import hardcoder.dev.logic.dashboard.features.diary.AttachmentTypeIdMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiaryAttachmentCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val attachmentTypeIdMapper: AttachmentTypeIdMapper
) {

    suspend fun create(
        attachmentId: Int,
        attachmentType: AttachmentType,
        diaryTrackId: Int
    ) = withContext(dispatcher) {
        appDatabase.diaryAttachmentQueries.create(
            id = idGenerator.nextId(),
            diaryTrackId = diaryTrackId,
            attachmentTypeId = attachmentTypeIdMapper.mapToId(attachmentType),
            attachmentId = attachmentId
        )
    }
}