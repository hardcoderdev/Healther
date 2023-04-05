package hardcoder.dev.logic.features.moodTracking.moodType

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MoodTypeCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val predefinedMoodTypeProvider: PredefinedMoodTypeProvider
) {

    suspend fun create(
        name: String,
        iconId: Long,
        positivePercentage: Int
    ) = withContext(dispatcher) {
        appDatabase.moodTypeQueries
            .insert(
                id = idGenerator.nextId(),
                name = name,
                iconId = iconId,
                positivePercentage = positivePercentage
            )
    }

    suspend fun createPredefined() = withContext(dispatcher) {
        predefinedMoodTypeProvider.providePredefined().forEach { moodType ->
            appDatabase.moodTypeQueries.insert(
                id = idGenerator.nextId(),
                name = moodType.name,
                iconId = moodType.icon.id,
                positivePercentage = moodType.positivePercentage
            )
        }
    }
}