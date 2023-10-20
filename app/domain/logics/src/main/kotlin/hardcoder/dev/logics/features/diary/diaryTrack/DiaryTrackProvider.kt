package hardcoder.dev.logics.features.diary.diaryTrack

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.diary.DiaryTrackDao
import hardcoder.dev.database.entities.features.diary.DiaryTrack
import hardcoder.dev.entities.features.diary.DiaryAttachmentGroup
import hardcoder.dev.logics.features.diary.diaryAttachment.DiaryAttachmentProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import hardcoder.dev.entities.features.diary.DiaryTrack as DiaryTrackEntity

@OptIn(ExperimentalCoroutinesApi::class)
class DiaryTrackProvider(
    private val diaryTrackDao: DiaryTrackDao,
    private val diaryAttachmentProvider: DiaryAttachmentProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAllDiaryTracksByDateRange(
        dateRange: ClosedRange<Instant>,
    ) = diaryTrackDao
        .provideAllDiaryTracksByDateRange(dateRange.start, dateRange.endInclusive)
        .flatMapLatest { diaryTracks ->
            if (diaryTracks.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    diaryTracks.map { diaryTrack ->
                        diaryAttachmentProvider.provideAttachmentOfDiaryTrackById(diaryTrack.id).map {
                            diaryTrack.toDiaryTrackEntity(
                                diaryAttachmentGroup = it,
                            )
                        }
                    },
                ) {
                    it.toList()
                }
            }
        }.flowOn(dispatchers.io)

    fun provideDiaryTrackById(id: Int) = diaryTrackDao
        .provideDiaryTrackById(id)
        .flatMapLatest { diaryTrackDatabase ->
            diaryTrackDatabase?.let {
                diaryAttachmentProvider.provideAttachmentOfDiaryTrackById(diaryTrackDatabase.id).map {
                    diaryTrackDatabase.toDiaryTrackEntity(
                        diaryAttachmentGroup = it,
                    )
                }
            } ?: flowOf(null)
        }.flowOn(dispatchers.io)

    private fun DiaryTrack.toDiaryTrackEntity(
        diaryAttachmentGroup: DiaryAttachmentGroup?,
    ) = DiaryTrackEntity(
        id = id,
        content = content,
        creationInstant = creationInstant,
        diaryAttachmentGroup = diaryAttachmentGroup,
    )
}