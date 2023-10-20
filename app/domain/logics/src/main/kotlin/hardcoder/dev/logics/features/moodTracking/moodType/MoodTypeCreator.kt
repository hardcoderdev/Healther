package hardcoder.dev.logics.features.moodTracking.moodType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.moodTracking.MoodTypeDao
import hardcoder.dev.database.entities.features.moodTracking.MoodType
import hardcoder.dev.icons.Icon
import hardcoder.dev.validators.features.moodTracking.CorrectMoodTypeName
import kotlinx.coroutines.withContext

class MoodTypeCreator(
    private val moodTypeDao: MoodTypeDao,
    private val predefinedMoodTypeProvider: PredefinedMoodTypeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        name: CorrectMoodTypeName,
        icon: Icon,
        positiveIndex: Int,
    ) = withContext(dispatchers.io) {
        moodTypeDao.insert(
            MoodType(
                name = name.data,
                iconId = icon.id,
                positivePercentage = positiveIndex,
            ),
        )
    }

    // TODO PRE-POPULATE CALLBACK ROOM
    suspend fun createPredefined() = withContext(dispatchers.io) {
        predefinedMoodTypeProvider.providePredefined().forEach { moodType ->
            moodTypeDao.insert(
                MoodType(
                    name = moodType.name,
                    iconId = moodType.icon.id,
                    positivePercentage = moodType.positivePercentage,
                ),
            )
        }
    }
}