package hardcoder.dev.logic.dashboard.features.diary.diaryAttachment

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.DiaryAttachment
import hardcoder.dev.logic.dashboard.features.diary.AttachmentType
import hardcoder.dev.logic.dashboard.features.diary.AttachmentTypeIdMapper
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.logic.features.fasting.track.FastingTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrack
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachmentGroup as AttachmentEntity
import hardcoder.dev.logic.dashboard.features.diary.diaryAttachment.DiaryAttachment as DiaryAttachmentEntity
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class DiaryAttachmentProvider(
    private val appDatabase: AppDatabase,
    private val attachmentTypeIdMapper: AttachmentTypeIdMapper,
    private val fastingTrackProvider: FastingTrackProvider,
    private val moodTrackProvider: MoodTrackProvider,
    private val diaryTagProvider: DiaryTagProvider
) {

    fun provideAttachmentByEntityId(attachmentType: AttachmentType, entityId: Int) =
        appDatabase.diaryAttachmentQueries
            .provideAttachmentByEntityId(
                targetTypeId = attachmentTypeIdMapper.mapToId(attachmentType),
                targetId = entityId
            )
            .asFlow()
            .map { it.executeAsOneOrNull() }
            .flatMapLatest { attachmentDatabase ->
                if (attachmentDatabase == null) flowOf(null)
                else {
                    flowOf(
                        DiaryAttachmentEntity(
                            id = attachmentDatabase.id,
                            diaryTrackId = attachmentDatabase.diaryTrackId,
                            targetType = attachmentType,
                            targetId = attachmentDatabase.targetId
                        )
                    )
                }
            }

    fun provideAttachmentOfDiaryTrackById(id: Int) = appDatabase.diaryAttachmentQueries
        .provideAllAttachmentsOfDiaryTrackById(id)
        .asFlow()
        .map { it.executeAsList() }
        .flatMapLatest { attachments ->
            if (attachments.isEmpty()) flowOf(null)
            else {
                combine(
                    attachments.map {
                        provideTargetEntity(attachment = it)
                    }
                ) { targetEntityArray ->
                    AttachmentEntity(
                        fastingTracks = targetEntityArray.filterIsInstance<FastingTrack>(),
                        moodTracks = targetEntityArray.filterIsInstance<MoodTrack>(),
                        tags = targetEntityArray.filterIsInstance<DiaryTag>()
                    )
                }
            }
        }

    private fun provideTargetEntity(attachment: DiaryAttachment): Flow<Any?> {
        return when (attachmentTypeIdMapper.mapToType(attachment.targetTypeId)) {
            AttachmentType.FASTING_ENTITY -> {
                fastingTrackProvider.provideFastingTrackById(attachment.targetId)
            }

            AttachmentType.MOOD_TRACKING_ENTITY -> {
                moodTrackProvider.provideById(attachment.targetId)
            }

            AttachmentType.TAG -> {
                diaryTagProvider.provideDiaryTagById(attachment.targetId)
            }
        }
    }
}