package hardcoder.dev.logic.features.moodTracking.moodType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.withContext

class MoodTypeCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val predefinedMoodTypeProvider: PredefinedMoodTypeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    suspend fun create(
        name: CorrectMoodTypeName,
        icon: LocalIcon,
        positiveIndex: Int
    ) = withContext(dispatchers.io) {
        appDatabase.moodTypeQueries.insert(
            id = idGenerator.nextId(),
            name = name.data,
            iconId = icon.id,
            positivePercentage = positiveIndex
        )
    }

    suspend fun createPredefined() = withContext(dispatchers.io) {
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