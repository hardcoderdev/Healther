package hardcoder.dev.logic.features.diary.diaryTrack

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.flow.map
import hardcoder.dev.database.DiaryTrack
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentGroup
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Instant
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrack as DiaryTrackEntity

@OptIn(ExperimentalCoroutinesApi::class)
class DiaryTrackProvider(
    private val appDatabase: AppDatabase,
    private val diaryAttachmentProvider: DiaryAttachmentProvider,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun provideAllDiaryTracksByDateRange(
        dateRange: ClosedRange<Instant>
    ) = appDatabase.diaryTrackQueries
        .provideAllDiaryTracksByDateRange(dateRange.start, dateRange.endInclusive)
        .asFlow()
        .map { it.executeAsList() }
        .flatMapLatest { diaryTracks ->
            if (diaryTracks.isEmpty()) flowOf(emptyList())
            else {
                combine(
                    diaryTracks.map { diaryTrack ->
                        diaryAttachmentProvider.provideAttachmentOfDiaryTrackById(diaryTrack.id).map { attachedEntity ->
                            diaryTrack.toDiaryTrackEntity(attachedEntity)
                        }
                    }
                ) {
                    it.toList()
                }
            }
        }.flowOn(ioDispatcher)

    fun provideDiaryTrackById(id: Int) = appDatabase.diaryTrackQueries
        .provideDiaryTrackById(id)
        .asFlow()
        .map { it.executeAsOneOrNull() }
        .flatMapLatest { diaryTrackDatabase ->
            diaryTrackDatabase?.let {
                diaryAttachmentProvider.provideAttachmentOfDiaryTrackById(diaryTrackDatabase.id).map { attachedEntity ->
                    diaryTrackDatabase.toDiaryTrackEntity(attachedEntity)
                }
            } ?: flowOf(null)
        }.flowOn(ioDispatcher)

    private fun DiaryTrack.toDiaryTrackEntity(diaryAttachmentGroup: DiaryAttachmentGroup?) = DiaryTrackEntity(
        id = id,
        content = content,
        date = date,
        diaryAttachmentGroup = diaryAttachmentGroup
    )
}