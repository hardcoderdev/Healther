package hardcoder.dev.healther.di

import android.content.Context
import hardcoder.dev.healther.repository.UserRepository
import hardcoder.dev.healther.repository.WaterTrackRepository
import kotlinx.coroutines.Dispatchers

class RepositoryModule(
    private val context: Context,
    private val databaseModule: DatabaseModule
) {

    val userRepository by lazy {
        UserRepository(
            context = context,
            dispatcher = Dispatchers.IO
        )
    }

    val waterTrackRepository by lazy {
        WaterTrackRepository(
            waterTrackDao = databaseModule.waterTrackDao,
            dispatcher = Dispatchers.IO
        )
    }
}