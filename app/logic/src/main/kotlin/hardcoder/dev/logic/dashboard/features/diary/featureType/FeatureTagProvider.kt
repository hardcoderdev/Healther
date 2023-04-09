package hardcoder.dev.logic.dashboard.features.diary.featureType

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.FeatureTag
import hardcoder.dev.extensions.mapItems
import kotlinx.coroutines.flow.map
import hardcoder.dev.logic.dashboard.features.diary.featureType.FeatureTag as FeatureTagEntity

class FeatureTagProvider(private val appDatabase: AppDatabase) {

    fun provideAllFeatureTags() = appDatabase.featureTagQueries
        .provideAllFeatureTags()
        .asFlow()
        .mapToList()
        .mapItems { it.toEntity() }

    fun provideFeatureTagById(id: Int) = appDatabase.featureTagQueries
        .provideFeatureTagById(id)
        .asFlow()
        .map { it.executeAsOneOrNull()?.toEntity() }

    private fun FeatureTag.toEntity() = FeatureTagEntity(
        id = id,
        name = name
    )
}