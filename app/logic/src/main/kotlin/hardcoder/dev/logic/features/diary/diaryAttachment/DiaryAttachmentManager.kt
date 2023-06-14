package hardcoder.dev.logic.features.diary.diaryAttachment

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.features.diary.AttachmentType
import hardcoder.dev.logic.features.diary.AttachmentTypeIdMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiaryAttachmentManager(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher,
    private val attachmentTypeIdMapper: AttachmentTypeIdMapper
) {

    suspend fun attach(
        diaryTrackId: Int,
        attachmentGroup: DiaryAttachmentGroup
    ) = withContext(ioDispatcher) {
        appDatabase.diaryAttachmentQueries.deleteByDiaryTrackId(diaryTrackId)

        attachmentGroup.moodTracks.forEach { moodTrack ->
            appDatabase.diaryAttachmentQueries.insert(
                id = idGenerator.nextId(),
                diaryTrackId = diaryTrackId,
                targetTypeId = attachmentTypeIdMapper.mapToId(AttachmentType.MOOD_TRACKING_ENTITY),
                targetId = moodTrack.id
            )
        }

        attachmentGroup.fastingTracks.forEach { fastingTrack ->
            appDatabase.diaryAttachmentQueries.insert(
                id = idGenerator.nextId(),
                diaryTrackId = diaryTrackId,
                targetTypeId = attachmentTypeIdMapper.mapToId(AttachmentType.FASTING_ENTITY),
                targetId = fastingTrack.id
            )
        }

        attachmentGroup.tags.forEach { tag ->
            appDatabase.diaryAttachmentQueries.insert(
                id = idGenerator.nextId(),
                diaryTrackId = diaryTrackId,
                targetTypeId = attachmentTypeIdMapper.mapToId(AttachmentType.TAG),
                targetId = tag.id
            )
        }
    }
}