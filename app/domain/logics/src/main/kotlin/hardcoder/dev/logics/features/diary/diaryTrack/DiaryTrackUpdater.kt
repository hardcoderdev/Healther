package hardcoder.dev.logics.features.diary.diaryTrack

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.diary.DiaryTrackDao
import hardcoder.dev.database.entities.features.diary.DiaryTrack
import hardcoder.dev.entities.features.diary.DiaryAttachmentGroup
import hardcoder.dev.logic.features.diary.diaryTrack.CorrectDiaryTrackContent
import hardcoder.dev.logics.features.diary.diaryAttachment.DiaryAttachmentManager
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class DiaryTrackUpdater(
    private val diaryTrackDao: DiaryTrackDao
    private val diaryAttachmentManager: DiaryAttachmentManager,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        id: Int,
        content: CorrectDiaryTrackContent,
        creationInstant: Instant,
        diaryAttachmentGroup: DiaryAttachmentGroup,
    ) = withContext(dispatchers.io) {
        diaryTrackDao.update(
            DiaryTrack(
                id = id,
                content = content.data,
                creationInstant = creationInstant,
            )
        )

        diaryAttachmentManager.attach(
            diaryTrackId = id,
            attachmentGroup = diaryAttachmentGroup,
        )
    }
}