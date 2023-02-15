package hardcoder.dev.healther.di

import android.content.Context
import hardcoder.dev.healther.repository.UserRepository
import hardcoder.dev.healther.repository.WaterTrackRepository
import kotlinx.coroutines.Dispatchers
import kotlin.math.log

class RepositoryModule(
    private val context: Context,
    private val logicModule: LogicModule,
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
            waterPercentageResolver = logicModule.waterPercentageResolver,
            drinkTypeImageResolver = logicModule.drinkTypeImageResolver,
            dispatcher = Dispatchers.IO
        )
    }
}