package hardcoder.dev.logic.dashboard.features.diary.diaryTrack

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.flow.map
import hardcoder.dev.database.DiaryTrack
import kotlinx.datetime.Instant
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrack as DiaryTrackEntity

class DiaryTrackProvider(private val appDatabase: AppDatabase) {

    fun provideAllDiaryTracksByDateRange(
        dateRange: ClosedRange<Instant>
    ) = appDatabase.diaryTrackQueries
        .provideAllDiaryEntriesByDateRange(dateRange.start, dateRange.endInclusive)
        .asFlow()
        .map {
            it.executeAsList().map { diaryTrackDatabase ->
                diaryTrackDatabase.toDiaryTrackEntity()
            }
        }

    fun provideDiaryTrackById(id: Int) = appDatabase.diaryTrackQueries
        .provideDiaryTrackById(id)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toDiaryTrackEntity()
        }

    private fun DiaryTrack.toDiaryTrackEntity() = DiaryTrackEntity(
        id = id,
        text = text,
        date = date,
        title = title
    )
}