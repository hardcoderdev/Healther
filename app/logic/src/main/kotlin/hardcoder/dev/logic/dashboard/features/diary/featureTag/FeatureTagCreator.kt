package hardcoder.dev.logic.dashboard.features.diary.featureTag

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FeatureTagCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val predefinedFeatureTagProvider: PredefinedFeatureTagProvider
) {

    suspend fun create(name: String) = withContext(dispatcher) {
        appDatabase.featureTagQueries.insert(id = idGenerator.nextId(), name = name)
    }

    suspend fun createPredefined() = withContext(dispatcher) {
        predefinedFeatureTagProvider.providePredefined().forEach { featureTag ->
            appDatabase.featureTagQueries.insert(
                id = idGenerator.nextId(),
                name = featureTag.name
            )
        }
    }
}