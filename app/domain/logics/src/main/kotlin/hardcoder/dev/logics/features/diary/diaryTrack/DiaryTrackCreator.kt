package hardcoder.dev.logics.features.diary.diaryTrack

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.diary.DiaryTrackDao
import hardcoder.dev.database.entities.features.diary.DiaryTrack
import hardcoder.dev.entities.features.diary.DiaryAttachmentGroup
import hardcoder.dev.logics.features.diary.diaryAttachment.DiaryAttachmentManager
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class DiaryTrackCreator(
    private val diaryTrackDao: DiaryTrackDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val diaryAttachmentManager: DiaryAttachmentManager,
) {

    suspend fun create(
        diaryAttachmentGroup: DiaryAttachmentGroup,
        date: Instant,
        content: String,
    ) = withContext(dispatchers.io) {
        val diaryTrackId = diaryTrackDao.insert(
            DiaryTrack(
                content = content,
                creationInstant = date,
            )
        )

        diaryAttachmentManager.attach(
            diaryTrackId = diaryTrackId.toInt(),
            attachmentGroup = diaryAttachmentGroup,
        )
    }
}