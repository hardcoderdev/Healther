package hardcoder.dev.logics.features.diary.diaryAttachment

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.entities.features.diary.AttachmentType
import hardcoder.dev.entities.features.diary.DiaryAttachmentGroup
import hardcoder.dev.identification.IdGenerator
import hardcoder.dev.mappers.features.diary.AttachmentTypeIdMapper
import kotlinx.coroutines.withContext

class DiaryAttachmentManager(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val attachmentTypeIdMapper: AttachmentTypeIdMapper,
) {

    suspend fun attach(
        diaryTrackId: Int,
        attachmentGroup: DiaryAttachmentGroup,
    ) = withContext(dispatchers.io) {
        appDatabase.diaryAttachmentQueries.deleteByDiaryTrackId(diaryTrackId)

        attachmentGroup.moodTracks.forEach { moodTrack ->
            appDatabase.diaryAttachmentQueries.insert(
                id = idGenerator.nextId(),
                diaryTrackId = diaryTrackId,
                targetTypeId = attachmentTypeIdMapper.mapToId(AttachmentType.MOOD_TRACKING_ENTITY),
                targetId = moodTrack.id,
            )
        }

        attachmentGroup.tags.forEach { tag ->
            appDatabase.diaryAttachmentQueries.insert(
                id = idGenerator.nextId(),
                diaryTrackId = diaryTrackId,
                targetTypeId = attachmentTypeIdMapper.mapToId(AttachmentType.TAG),
                targetId = tag.id,
            )
        }
    }
}