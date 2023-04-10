package hardcoder.dev.logic.dashboard.features.diary.diaryTrack

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.dashboard.features.diary.diaryWithFeatureType.DiaryWithFeatureTagsCreator
import hardcoder.dev.logic.dashboard.features.diary.diaryWithFeatureType.DiaryWithFeatureTagsDeleter
import hardcoder.dev.logic.dashboard.features.diary.featureTag.FeatureTag
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiaryTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val diaryWithFeatureTagsCreator: DiaryWithFeatureTagsCreator,
    private val diaryWithFeatureTagsDeleter: DiaryWithFeatureTagsDeleter
) {

    suspend fun update(
        diaryTrack: DiaryTrack,
        selectedFeatureTags: List<FeatureTag>
    ) = withContext(dispatcher) {
        appDatabase.diaryTrackQueries.update(
            id = diaryTrack.id,
            text = diaryTrack.text,
            title = diaryTrack.title
        )

        diaryWithFeatureTagsDeleter.deleteAllDiaryWithFeatureTagsByDiaryId(diaryTrack.id)

        selectedFeatureTags.forEach { featureTag ->
            diaryWithFeatureTagsCreator.create(
                diaryTrackId = diaryTrack.id,
                featureTagId = featureTag.id
            )
        }
    }
}