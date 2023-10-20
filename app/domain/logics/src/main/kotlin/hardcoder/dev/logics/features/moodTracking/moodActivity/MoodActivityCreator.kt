package hardcoder.dev.logics.features.moodTracking.moodActivity

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.moodTracking.MoodActivityDao
import hardcoder.dev.database.entities.features.moodTracking.MoodActivity
import hardcoder.dev.icons.Icon
import hardcoder.dev.validators.features.moodTracking.CorrectActivityName
import kotlinx.coroutines.withContext

class MoodActivityCreator(
    private val moodActivityDao: MoodActivityDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        name: CorrectActivityName,
        icon: Icon,
    ) = withContext(dispatchers.io) {
        moodActivityDao.insert(
            MoodActivity(
                name = name.data,
                iconId = icon.id,
            ),
        )
    }
}