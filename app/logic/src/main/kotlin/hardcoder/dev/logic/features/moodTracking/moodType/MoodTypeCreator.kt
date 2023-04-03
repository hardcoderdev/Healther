package hardcoder.dev.logic.features.moodTracking.moodType

import android.content.Context
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.R
import hardcoder.dev.logic.entities.features.moodTracking.MoodType
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MoodTypeCreator(
    private val context: Context,
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val iconResourceProvider: IconResourceProvider
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
        generatePredefinedMoodTypes().forEach { moodType ->
            appDatabase.moodTypeQueries.insert(
                id = moodType.id,
                name = moodType.name,
                iconId = moodType.icon.id,
                positivePercentage = moodType.positivePercentage
            )
        }
    }

    // TODO ICONS FROM DESIGNER WILL BE HERE
    private fun generatePredefinedMoodTypes() = listOf(
        MoodType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_moodType_name_happy),
            icon = iconResourceProvider.getIcon(0),
            positivePercentage = 100
        ),
        MoodType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_moodType_name_neutral),
            icon = iconResourceProvider.getIcon(1),
            positivePercentage = 80
        ),
        MoodType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_moodType_name_not_well),
            icon = iconResourceProvider.getIcon(2),
            positivePercentage = 60
        ),
        MoodType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_moodType_name_bad),
            icon = iconResourceProvider.getIcon(3),
            positivePercentage = 40
        )
    )
}