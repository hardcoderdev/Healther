package hardcoder.dev.logic.features.moodTracking.moodType

import android.content.Context
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.entities.features.moodTracking.MoodType
import hardcoder.dev.logic.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MoodTypeCreator(
    private val context: Context,
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun create(
        name: String,
        iconResourceName: String,
        positivePercentage: Int
    ) = withContext(dispatcher) {
        appDatabase.moodTypeQueries
            .insert(
                id = idGenerator.nextId(),
                name = name,
                iconResourceName = iconResourceName,
                positivePercentage = positivePercentage
            )
    }

    suspend fun createPredefined() = withContext(dispatcher) {
        generatePredefinedMoodTypes().forEach { moodType ->
            appDatabase.moodTypeQueries.insert(
                id = moodType.id,
                name = moodType.name,
                iconResourceName = moodType.iconResourceName,
                positivePercentage = moodType.positivePercentage
            )
        }
    }

    // TODO ICONS FROM DESIGNER WILL BE HERE
    private fun generatePredefinedMoodTypes() = listOf(
        MoodType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_moodType_name_happy),
            iconResourceName = "mood_happy",
            positivePercentage = 100
        ),
        MoodType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_moodType_name_neutral),
            iconResourceName = "mood_neutral",
            positivePercentage = 80
        ),
        MoodType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_moodType_name_not_well),
            iconResourceName = "mood_not_well",
            positivePercentage = 60
        ),
        MoodType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_moodType_name_bad),
            iconResourceName = "mood_bad",
            positivePercentage = 40
        )
    )
}