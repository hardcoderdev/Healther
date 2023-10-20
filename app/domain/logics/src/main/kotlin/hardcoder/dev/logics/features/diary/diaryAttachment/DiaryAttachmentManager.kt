package hardcoder.dev.logics.features.diary.diaryAttachment

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.diary.DiaryAttachmentDao
import hardcoder.dev.database.entities.features.diary.DiaryAttachment
import hardcoder.dev.entities.features.diary.AttachmentType
import hardcoder.dev.entities.features.diary.DiaryAttachmentGroup
import hardcoder.dev.mappers.features.diary.AttachmentTypeIdMapper
import kotlinx.coroutines.withContext

class DiaryAttachmentManager(
    private val diaryAttachmentDao: DiaryAttachmentDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val attachmentTypeIdMapper: AttachmentTypeIdMapper,
) {

    suspend fun attach(
        diaryTrackId: Int,
        attachmentGroup: DiaryAttachmentGroup,
    ) = withContext(dispatchers.io) {
        diaryAttachmentDao.provideDiaryAttachmentsByDiaryTrackId(diaryTrackId)

        attachmentGroup.moodTracks.forEach { moodTrack ->
            diaryAttachmentDao.insert(
                DiaryAttachment(
                    diaryTrackId = diaryTrackId,
                    targetTypeId = attachmentTypeIdMapper.mapToId(AttachmentType.MOOD_TRACKING_ENTITY),
                    targetId = moodTrack.id,
                ),
            )
        }

        attachmentGroup.tags.forEach { tag ->
            diaryAttachmentDao.insert(
                DiaryAttachment(
                    diaryTrackId = diaryTrackId,
                    targetTypeId = attachmentTypeIdMapper.mapToId(AttachmentType.TAG),
                    targetId = tag.id,
                ),
            )
        }
    }
}