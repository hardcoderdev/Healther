package hardcoder.dev.logic.features.moodTracking.moodType

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MoodTypeCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher,
    private val predefinedMoodTypeProvider: PredefinedMoodTypeProvider
) {

    suspend fun create(
        name: CorrectMoodTypeName,
        icon: LocalIcon,
        positiveIndex: Int
    ) = withContext(ioDispatcher) {
        appDatabase.moodTypeQueries
            .insert(
                id = idGenerator.nextId(),
                name = name.data,
                iconId = icon.id,
                positivePercentage = positiveIndex
            )
    }

    suspend fun createPredefined() = withContext(ioDispatcher) {
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