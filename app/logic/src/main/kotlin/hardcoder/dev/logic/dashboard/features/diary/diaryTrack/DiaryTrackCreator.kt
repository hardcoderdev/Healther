package hardcoder.dev.logic.dashboard.features.diary.diaryTrack

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.dashboard.features.diary.diaryWithFeatureType.DiaryWithFeatureTagsCreator
import hardcoder.dev.logic.dashboard.features.diary.featureTag.FeatureTag
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class DiaryTrackCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val diaryWithFeatureTagsCreator: DiaryWithFeatureTagsCreator
) {

    suspend fun create(
        date: Instant,
        text: String,
        title: String?,
        selectedFeatureTags: List<FeatureTag>
    ) = withContext(dispatcher) {
        val diaryTrackId = idGenerator.nextId()
        appDatabase.diaryTrackQueries.insert(
            id = diaryTrackId,
            date = date,
            text = text,
            title = title
        )

        selectedFeatureTags.forEach { featureTag ->
            diaryWithFeatureTagsCreator.create(
                diaryTrackId = diaryTrackId,
                featureTagId = featureTag.id
            )
        }
    }
}