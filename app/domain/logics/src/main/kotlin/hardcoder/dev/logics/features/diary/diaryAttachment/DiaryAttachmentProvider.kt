package hardcoder.dev.logics.features.diary.diaryAttachment

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.DiaryAttachment
import hardcoder.dev.entities.features.diary.AttachmentType
import hardcoder.dev.entities.features.diary.DiaryTag
import hardcoder.dev.entities.features.moodTracking.MoodTrack
import hardcoder.dev.logics.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logics.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.mappers.features.diary.AttachmentTypeIdMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.diary.DiaryAttachment as DiaryAttachmentEntity
import hardcoder.dev.entities.features.diary.DiaryAttachmentGroup as AttachmentEntity

@OptIn(ExperimentalCoroutinesApi::class)
class DiaryAttachmentProvider(
    private val appDatabase: AppDatabase,
    private val attachmentTypeIdMapper: AttachmentTypeIdMapper,
    private val moodTrackProvider: MoodTrackProvider,
    private val diaryTagProvider: DiaryTagProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAttachmentByEntityId(attachmentType: AttachmentType, entityId: Int) =
        appDatabase.diaryAttachmentQueries
            .selectByTarget(
                targetTypeId = attachmentTypeIdMapper.mapToId(attachmentType),
                targetId = entityId,
            )
            .asFlow()
            .map {
                it.executeAsOneOrNull()?.let { attachment ->
                    DiaryAttachmentEntity(
                        id = attachment.id,
                        diaryTrackId = attachment.diaryTrackId,
                        targetType = attachmentType,
                        targetId = attachment.targetId,
                    )
                }
            }.flowOn(dispatchers.io)

    fun provideAttachmentOfDiaryTrackById(id: Int) = appDatabase.diaryAttachmentQueries
        .selectByDiaryTrackId(id)
        .asFlow()
        .map { it.executeAsList() }
        .flatMapLatest { attachments ->
            if (attachments.isEmpty()) {
                flowOf(null)
            } else {
                combine(
                    attachments.map {
                        provideTargetEntity(attachment = it)
                    },
                ) { targetEntityArray ->
                    AttachmentEntity(
                        moodTracks = targetEntityArray.filterIsInstance<MoodTrack>(),
                        tags = targetEntityArray.filterIsInstance<DiaryTag>().toSet(),
                    )
                }
            }
        }.flowOn(dispatchers.io)

    private fun provideTargetEntity(attachment: DiaryAttachment): Flow<Any?> {
        return when (attachmentTypeIdMapper.mapToAttachmentType(attachment.targetTypeId)) {
            AttachmentType.MOOD_TRACKING_ENTITY -> {
                moodTrackProvider.provideById(attachment.targetId)
            }

            AttachmentType.TAG -> {
                diaryTagProvider.provideDiaryTagById(attachment.targetId)
            }
        }.flowOn(dispatchers.io)
    }
}