package hardcoder.dev.healther.di

import android.content.Context
import hardcoder.dev.healther.repository.UserRepository
import hardcoder.dev.healther.repository.WaterTrackRepository

class RepositoryModule(
    private val context: Context,
    private val databaseModule: DatabaseModule
) {

    val userRepository by lazy {
        UserRepository(context)
    }

    val waterTrackRepository by lazy {
        WaterTrackRepository(databaseModule.waterTrackDao)
    }
}