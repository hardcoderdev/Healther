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
                attachmentTypeId = attachmentTypeIdMapper.mapToId(attachmentType),
                attachmentId = entityId
            )
            .asFlow()
            .map { it.executeAsOneOrNull() }
            .flatMapLatest { attachmentDatabase ->
                attachmentDatabase?.let {
                    combine(
                        when (attachmentTypeIdMapper.mapToType(attachmentDatabase.attachmentTypeId)) {
                            AttachmentType.FASTING_ENTITY -> {
                                fastingTrackProvider.provideFastingTrackById(attachmentDatabase.attachmentId)
                            }

                            AttachmentType.MOOD_TRACKING_ENTITY -> {
                                moodTrackProvider.provideById(attachmentDatabase.attachmentId)
                            }

                            AttachmentType.TAG -> {
                                diaryTagProvider.provideDiaryTagById(attachmentDatabase.attachmentId)
                            }
                        }
                    ) { attachments ->
                        AttachmentEntity(
                            diaryTrackId = attachmentDatabase.diaryTrackId,
                            fastingTracks = attachments.filterIsInstance<FastingTrack>(),
                            moodTracks = attachments.filterIsInstance<MoodTrack>(),
                            tags = attachments.filterIsInstance<DiaryTag>()
                        )
                    }
                } ?: flowOf(null)
            }

    fun provideAttachmentOfDiaryTrackById(id: Int) = appDatabase.diaryAttachmentQueries
        .provideAllAttachmentsOfDiaryTrackById(id)
        .asFlow()
        .map { it.executeAsList() }
        .flatMapLatest { attachments ->
            if (attachments.isEmpty()) flowOf(null)
            else {
                attachments.toDiaryAttachmentGroup(id)
            }
        }

    private fun List<DiaryAttachment>.toDiaryAttachmentGroup(id: Int): Flow<AttachmentEntity> {
        return combine(
            map { attachment ->
                when (attachmentTypeIdMapper.mapToType(attachment.attachmentTypeId)) {
                    AttachmentType.FASTING_ENTITY -> {
                        fastingTrackProvider.provideFastingTrackById(attachment.attachmentId)
                    }

                    AttachmentType.MOOD_TRACKING_ENTITY -> {
                        moodTrackProvider.provideById(attachment.attachmentId)
                    }

                    AttachmentType.TAG -> {
                        diaryTagProvider.provideDiaryTagById(attachment.attachmentId)
                    }
                }
            }) { attachments ->
            AttachmentEntity(
                diaryTrackId = id,
                fastingTracks = attachments.filterIsInstance<FastingTrack>(),
                moodTracks = attachments.filterIsInstance<MoodTrack>(),
                tags = attachments.filterIsInstance<DiaryTag>()
            )
        }
    }
}