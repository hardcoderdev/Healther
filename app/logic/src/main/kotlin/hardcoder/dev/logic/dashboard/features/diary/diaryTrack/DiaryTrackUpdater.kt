package hardcoder.dev.logic.dashboard.features.diary.diaryTrack

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.dashboard.features.diary.AttachmentType
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentCreator
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentDeleter
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTag
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
        selectedTags: List<DiaryTag>
    ) = withContext(dispatcher) {
        appDatabase.diaryTrackQueries.update(
            id = diaryTrack.id,
            description = diaryTrack.description,
            title = diaryTrack.title
        )

        diaryAttachmentDeleter.deleteAllTagsOfDiaryTrackById(diaryTrack.id)

        selectedTags.forEach { tag ->
            diaryAttachmentCreator.create(
                attachmentId = tag.id,
                attachmentType = AttachmentType.TAG,
                diaryTrackId = diaryTrack.id
            )
        }
    }
}