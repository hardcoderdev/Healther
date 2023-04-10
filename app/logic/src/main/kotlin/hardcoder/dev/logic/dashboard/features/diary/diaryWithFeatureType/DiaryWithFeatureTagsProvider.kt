package hardcoder.dev.logic.dashboard.features.diary.diaryWithFeatureType

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logic.dashboard.features.diary.featureTag.FeatureTagProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class DiaryWithFeatureTagsProvider(
    private val appDatabase: AppDatabase,
    private val diaryTrackProvider: DiaryTrackProvider,
    private val featureTagsProvider: FeatureTagProvider
) {

    fun provideDiaryWithFeatureTagList(dateRange: ClosedRange<Instant>): Flow<List<DiaryWithFeatureTags>> {
        return diaryTrackProvider.provideAllDiaryTracksByDateRange(dateRange = dateRange)
            .flatMapLatest { tracks ->
                if (tracks.isEmpty()) flowOf(emptyList())
                else combine(
                    tracks.map { diaryTrack ->
                        appDatabase.diaryWithFeatureTagQueries.provideAllDiaryWithFeatureTagsByDiaryTrackId(
                            diaryTrack.id
                        )
                            .asFlow()
                            .map { it.executeAsList() }
                            .flatMapLatest { diaryWithFeatureTags ->
                                if (diaryWithFeatureTags.isEmpty()) {
                                    flowOf(
                                        DiaryWithFeatureTags(
                                            diaryTrack = diaryTrack,
                                            featureTags = emptyList()
                                        )
                                    )
                                } else {
                                    combine(
                                        diaryWithFeatureTags.map {
                                            featureTagsProvider.provideFeatureTagById(it.featureTagId)
                                        }
                                    ) { featureTags ->
                                        DiaryWithFeatureTags(
                                            diaryTrack = diaryTrack,
                                            featureTags = featureTags.toList().filterNotNull()
                                        )
                                    }
                                }
                            }
                    }
                ) {
                    it.toList()
                }
            }
    }

    fun provideFeatureTagListByDiaryTrackId(id: Int) = appDatabase.diaryWithFeatureTagQueries
        .provideAllDiaryWithFeatureTagsByDiaryTrackId(id)
        .asFlow()
        .mapToList()
        .flatMapLatest { diaryWithFeatureTags ->
            if (diaryWithFeatureTags.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    diaryWithFeatureTags.map {
                        featureTagsProvider.provideFeatureTagById(it.featureTagId)
                    }
                ) { featureTags ->
                    featureTags.toList().filterNotNull()
                }
            }
        }
}