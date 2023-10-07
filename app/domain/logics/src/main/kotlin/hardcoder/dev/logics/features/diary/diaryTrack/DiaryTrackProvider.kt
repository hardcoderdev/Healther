package hardcoder.dev.logic.features.diary.diaryTrack

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.DiaryTrack
import hardcoder.dev.entities.features.diary.DiaryAttachmentGroup
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentProvider
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
    private val appDatabase: AppDatabase,
    private val diaryAttachmentProvider: DiaryAttachmentProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAllDiaryTracksByDateRange(
        dateRange: ClosedRange<Instant>,
    ) = appDatabase.diaryTrackQueries
        .provideAllDiaryTracksByDateRange(dateRange.start, dateRange.endInclusive)
        .asFlow()
        .map { it.executeAsList() }
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

    fun provideDiaryTrackById(id: Int) = appDatabase.diaryTrackQueries
        .provideDiaryTrackById(id)
        .asFlow()
        .map { it.executeAsOneOrNull() }
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
        date = date,
        diaryAttachmentGroup = diaryAttachmentGroup,
    )
}